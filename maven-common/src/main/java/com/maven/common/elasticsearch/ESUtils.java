package com.maven.common.elasticsearch;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maven.common.StringUtils;
import com.maven.common.elasticsearch.entity.HighLightEntity;
import com.maven.common.elasticsearch.entity.QueryEntity;
import com.maven.common.elasticsearch.entity.SearchEntity;
import com.maven.common.elasticsearch.entity.SortEntity;
import com.maven.common.page.Pager;
import com.maven.common.properties.LoadPropertiesUtils;
import com.maven.common.request.MapUtils;

/**
 * ES搜索引擎类
 * 
 * @author chenjian
 * @createDate 2019-10-25
 */
public class ESUtils {

	private static Logger logger = LoggerFactory.getLogger(ESUtils.class);

	// ES服务器地址
	private String host;

	// ES服务器端口
	private int port = 0;

	// ES的Index名称
	private String index;

	// ES默认服务器地址
	private final String DEFAULT_HOST = "127.0.0.1";

	// ES默认服务器端口
	private final int DEFAULT_PORT = 9200;

	// ES默认Index名称
	private final String DEFAULT_INDEX = "studentindex";

	// RestHighLevelClient实例类
	private RestHighLevelClient client = null;

	/**
	 * 构造函数
	 */
	public ESUtils() {
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            ES服务器地址
	 * @param port
	 *            ES服务器端口
	 */
	public ESUtils(String host, int port) {
		this.host = host;
		this.port = port;
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param index
	 *            ES的Index名称
	 */
	public ESUtils(String index) {
		this.index = index;
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            ES服务器地址
	 * @param port
	 *            ES服务器端口
	 * @param index
	 *            ES的Index名称
	 */
	public ESUtils(String host, int port, String index) {
		this.host = host;
		this.port = port;
		this.index = index;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		LoadPropertiesUtils loadPropertiesUtils = LoadPropertiesUtils
				.getInstance("es.properties");

		this.host = StringUtils.isNotEmpty(this.host) ? this.host
				: StringUtils.isNotEmpty(loadPropertiesUtils.getKey("host")) ? loadPropertiesUtils
						.getKey("host") : DEFAULT_HOST;
		this.port = this.port > 0 ? this.port : StringUtils
				.isNotEmpty(loadPropertiesUtils.getKey("port")) ? Integer
				.valueOf(loadPropertiesUtils.getKey("port")).intValue()
				: DEFAULT_PORT;
		this.index = StringUtils.isNotEmpty(this.index) ? this.index
				: StringUtils.isNotEmpty(loadPropertiesUtils.getKey("index")) ? loadPropertiesUtils
						.getKey("index") : DEFAULT_INDEX;
	}

	/**
	 * 根据ID获取数据
	 * 
	 * @param id
	 *            ID编号
	 * @return Map对象
	 */
	public Map<String, Object> findById(String id) {
		Map<String, Object> map = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(id)) {
				connect();

				GetRequest getRequest = new GetRequest(this.index, id);
				GetResponse getResponse = client.get(getRequest,
						RequestOptions.DEFAULT);
				map = getResponse.getSourceAsMap();

				message = "Find single data success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Find single data error");
		} finally {
			disconnect();
		}

		return map;
	}

	/**
	 * 分页查询
	 * 
	 * @param searchEntity
	 *            查询对象
	 * @param pageNo
	 *            当前页数
	 * @param size
	 *            每页数量
	 * @return Pager对象
	 */
	public Pager findPager(SearchEntity searchEntity, int pageNo, int size) {
		Pager pager = null;

		try {
			connect();

			SearchRequest searchRequest = new SearchRequest(this.index);
			SearchSourceBuilder searchSourceBuilder = createQuerySource(
					searchEntity, pageNo, size);
			searchRequest.source(searchSourceBuilder);

			SearchResponse searchResponse = client.search(searchRequest,
					RequestOptions.DEFAULT);
			SearchHits searchHits = searchResponse.getHits();

			// 设置分页数据
			pager = new Pager();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			// 判断是否有数据
			if (StringUtils.isNotEmpty(searchHits)
					&& searchHits.getTotalHits().value > 0) {
				for (SearchHit searchHit : searchHits) {
					Map<String, Object> map = searchHit.getSourceAsMap();
					Map<String, HighlightField> highlightFields = searchHit
							.getHighlightFields();

					if (highlightFields.size() > 0) {
						Iterator<Entry<String, HighlightField>> iterator = highlightFields
								.entrySet().iterator();

						while (iterator.hasNext()) {
							Entry<String, HighlightField> entry = iterator
									.next();
							map.put(entry.getKey(), entry.getValue()
									.fragments()[0].toString());
						}
					}

					list.add(map);
				}

				pager.setCurrentPage(pageNo);
				pager.setRowsTotal(Long
						.valueOf(searchHits.getTotalHits().value).intValue());
				pager.setPageSize(size);
				pager.setList(list);
			}

			logger.info("Find pager data success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Find pager data error");
		} finally {
			disconnect();
		}

		return pager;
	}

	/**
	 * 添加一条数据
	 * 
	 * @param jsonObject
	 *            数据对象
	 * @return 是否添加成功
	 */
	@SuppressWarnings("unchecked")
	public boolean addSingle(JSONObject jsonObject) {
		return addSingle(JSONObject.toJavaObject(jsonObject, Map.class));
	}

	/**
	 * 添加一条数据
	 * 
	 * @param map
	 *            数据对象
	 * @return 是否添加成功
	 */
	public boolean addSingle(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 判断数据对象是否存在
		if (StringUtils.isNotEmpty(map)) {
			list.add(map);
		}

		return addAll(list);
	}

	/**
	 * 添加多条数据
	 * 
	 * @param jsonArray
	 *            数据对象集合
	 * @return 是否添加成功
	 */
	@SuppressWarnings("unchecked")
	public boolean addAll(JSONArray jsonArray) {
		return addAll(JSONArray.toJavaObject(jsonArray, List.class));
	}

	/**
	 * 添加多条数据
	 * 
	 * @param list
	 *            数据对象集合
	 * @return 是否添加成功
	 */
	public boolean addAll(List<Map<String, Object>> list) {
		boolean result = false;

		try {
			String message = "No data";

			// 判断传入参数
			if (StringUtils.isNotEmpty(list) && list.size() > 0) {
				connect();

				BulkRequest bulkRequest = new BulkRequest();

				for (Map<String, Object> source : list) {
					String id = MapUtils.getString(source, "id");

					if (StringUtils.isNotEmpty(id)) {
						IndexRequest indexRequest = new IndexRequest(this.index)
								.id(id).source(source);
						bulkRequest.add(indexRequest);
					}
				}

				BulkResponse bulkResponse = client.bulk(bulkRequest,
						RequestOptions.DEFAULT);

				createLogger(bulkResponse);
				result = true;

				message = "Add data success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Add data error");
		} finally {
			disconnect();
		}

		return result;
	}

	/**
	 * 修改一条数据
	 * 
	 * @param jsonObject
	 *            数据对象
	 * @return 是否修改成功
	 */
	@SuppressWarnings("unchecked")
	public boolean updateSingle(JSONObject jsonObject) {
		return updateSingle(JSONObject.toJavaObject(jsonObject, Map.class));
	}

	/**
	 * 修改一条数据
	 * 
	 * @param map
	 *            数据对象
	 * @return 是否修改成功
	 */
	public boolean updateSingle(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 判断数据对象是否存在
		if (StringUtils.isNotEmpty(map)) {
			list.add(map);
		}

		return updateAll(list);
	}

	/**
	 * 修改多条数据
	 * 
	 * @param jsonArray
	 *            数据对象集合
	 * @return 是否修改成功
	 */
	@SuppressWarnings("unchecked")
	public boolean updateAll(JSONArray jsonArray) {
		return updateAll(JSONArray.toJavaObject(jsonArray, List.class));
	}

	/**
	 * 修改多条数据
	 * 
	 * @param list
	 *            数据对象集合
	 * @return 是否修改成功
	 */
	public boolean updateAll(List<Map<String, Object>> list) {
		boolean result = false;

		try {
			String message = "No data";

			// 判断传入参数
			if (StringUtils.isNotEmpty(list) && list.size() > 0) {
				connect();

				BulkRequest bulkRequest = new BulkRequest();

				for (Map<String, Object> source : list) {
					String id = MapUtils.getString(source, "id");

					if (StringUtils.isNotEmpty(id)) {
						UpdateRequest updateRequest = new UpdateRequest(
								this.index, id).doc(source);
						bulkRequest.add(updateRequest);
					}
				}

				BulkResponse bulkResponse = client.bulk(bulkRequest,
						RequestOptions.DEFAULT);

				createLogger(bulkResponse);
				result = true;

				message = "Update data success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update data error");
		} finally {
			disconnect();
		}

		return result;
	}

	/**
	 * 根据编号删除一个或多个数据
	 * 
	 * @param ids
	 *            ID编号集合
	 * @return 是否删除成功
	 */
	public boolean deleteByIds(String... ids) {
		boolean result = false;

		try {
			String message = "No data";

			// 判断传入参数
			if (StringUtils.isNotEmpty(ids) && ids.length > 0) {
				connect();

				BulkRequest bulkRequest = new BulkRequest();

				for (String id : ids) {
					if (StringUtils.isNotEmpty(id)) {
						DeleteRequest deleteRequest = new DeleteRequest(
								this.index, id);
						bulkRequest.add(deleteRequest);
					}
				}

				BulkResponse bulkResponse = client.bulk(bulkRequest,
						RequestOptions.DEFAULT);

				createLogger(bulkResponse);
				result = true;

				message = "Delete data success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete data error");
		} finally {
			disconnect();
		}

		return result;
	}

	/**
	 * 根据条件删除数据
	 * 
	 * @param queryBuilder
	 *            条件对象
	 * @return 是否删除成功
	 */
	public boolean deleteByQuery(QueryBuilder queryBuilder) {
		boolean result = false;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(queryBuilder)) {
				connect();

				DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(
						this.index);
				deleteByQueryRequest.setQuery(queryBuilder);
				deleteByQueryRequest.setRefresh(true);

				BulkByScrollResponse bulkByScrollResponse = client
						.deleteByQuery(deleteByQueryRequest,
								RequestOptions.DEFAULT);

				createLogger(bulkByScrollResponse);
				result = true;

				message = "Delete data success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete data error");
		} finally {
			disconnect();
		}

		return result;
	}

	/**
	 * 删除索引所有数据
	 * 
	 * @return 是否删除成功
	 */
	public boolean deleteAll() {
		return deleteByQuery(QueryBuilders.matchAllQuery());
	}

	/**
	 * 删除索引并删除所有数据
	 * 
	 * @return 是否删除成功
	 */
	public boolean deleteIndex() {
		boolean result = false;

		try {
			connect();

			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(
					this.index);
			client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

			logger.info("Delete index success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete index error");
		} finally {
			disconnect();
		}

		return result;
	}

	/**
	 * 生成查询Query
	 * 
	 * @param searchEntity
	 *            查询对象
	 * @param pageNo
	 *            当前页数
	 * @param size
	 *            每页数量
	 * @return SearchSourceBuilder对象
	 */
	private SearchSourceBuilder createQuerySource(SearchEntity searchEntity,
			int pageNo, int size) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
		HighlightBuilder highlightBuilder = null;

		// 判断是否有搜索对象
		if (StringUtils.isNotEmpty(searchEntity)) {
			QueryEntity query = searchEntity.getQuery();
			HighLightEntity highLight = searchEntity.getHighLight();
			List<SortEntity> sorts = searchEntity.getSorts();

			// 判断是否有搜索条件
			if (StringUtils.isNotEmpty(query)) {
				BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

				// 关键字搜索
				if (StringUtils.isNotEmpty(query.getKeyword())) {
					MultiMatchQueryBuilder keywordMMQB = QueryBuilders
							.multiMatchQuery(query.getKeyword(), "userName",
									"nickName", "sex", "host");
					boolQueryBuilder.must(keywordMMQB);
				}

				// 性别搜索
				if (StringUtils.isNotEmpty(query.getSex())) {
					TermQueryBuilder sexTQB = QueryBuilders.termQuery("sex",
							query.getSex());
					boolQueryBuilder.must(sexTQB);
				}

				// 年龄搜索
				if (query.getMinAge() > 0 || query.getMaxAge() > 0) {
					RangeQueryBuilder ageRQB = QueryBuilders.rangeQuery("age");

					// 最小年龄搜索
					if (query.getMinAge() > 0) {
						ageRQB.gte(query.getMinAge());
					}

					// 最大年龄搜索
					if (query.getMaxAge() > 0) {
						ageRQB.lte(query.getMaxAge());
					}

					boolQueryBuilder.must(ageRQB);
				}

				// 住址搜索
				if (StringUtils.isNotEmpty(query.getAddress())) {
					MatchPhraseQueryBuilder addressMPQB = QueryBuilders
							.matchPhraseQuery("address", query.getAddress());
					boolQueryBuilder.must(addressMPQB);
				}

				queryBuilder = boolQueryBuilder;
			}

			// 判断是否设置高亮
			if (StringUtils.isNotEmpty(highLight)) {
				highlightBuilder = new HighlightBuilder();
				List<String> fields = highLight.getFields();
				String preTags = highLight.getPreTags();
				String postTags = highLight.getPostTags();

				// 判断是否设置高亮字段
				if (StringUtils.isNotEmpty(fields) && fields.size() > 0) {
					for (String fieldName : fields) {
						highlightBuilder.field(fieldName);
					}
				}

				// 判断是否设置高亮标签
				if (StringUtils.isNotEmpty(preTags)
						&& StringUtils.isNotEmpty(postTags)) {
					highlightBuilder.preTags(preTags);
					highlightBuilder.postTags(postTags);
				}
			}

			// 判断是否设置排序
			if (StringUtils.isNotEmpty(sorts) && sorts.size() > 0) {
				for (SortEntity sortEntity : sorts) {
					searchSourceBuilder.sort(sortEntity.getFieldName(),
							sortEntity.getSort());
				}
			}
		}

		// 设置Query
		if (StringUtils.isNotEmpty(queryBuilder)) {
			searchSourceBuilder.query(queryBuilder);
		}

		// 设置高亮
		if (StringUtils.isNotEmpty(highlightBuilder)) {
			searchSourceBuilder.highlighter(highlightBuilder);
		}

		// 设置查询起始位置和查询数量
		pageNo = pageNo > 0 ? pageNo : 1;
		int start = (pageNo - 1) * size;
		searchSourceBuilder.from(start).size(size);

		return searchSourceBuilder;
	}

	/**
	 * 生成日志数据
	 * 
	 * @param bulkResponse
	 */
	private void createLogger(BulkResponse bulkResponse) {
		int addNum = 0, updateNum = 0, deleteNum = 0;
		String message = "No info";

		// 遍历Response
		for (BulkItemResponse bulkItemResponse : bulkResponse) {
			if (bulkItemResponse.isFailed()) {
				logger.info(bulkItemResponse.getFailureMessage());
				continue;
			}

			if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
					|| bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
				addNum += 1;
			} else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
				updateNum += 1;
			} else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
				deleteNum += 1;
			}
		}

		// 判断是否有日志数据
		if (addNum > 0 || updateNum > 0 || deleteNum > 0) {
			StringBuffer buffer = new StringBuffer();

			// 判断是否有新增
			if (addNum > 0) {
				buffer.append(StringUtils.isNotEmpty(buffer.toString()) ? ";"
						: "" + "Add data number："
								+ Integer.valueOf(addNum).toString());
			}

			// 判断是否有新增
			if (updateNum > 0) {
				buffer.append(StringUtils.isNotEmpty(buffer.toString()) ? ";"
						: "" + "Update data number："
								+ Integer.valueOf(updateNum).toString());
			}

			// 判断是否有新增
			if (deleteNum > 0) {
				buffer.append(StringUtils.isNotEmpty(buffer.toString()) ? ";"
						: "" + "Delete data number："
								+ Integer.valueOf(deleteNum).toString());
			}

			message = buffer.toString();
		}

		logger.info(message);
	}

	/**
	 * 生成日志数据
	 * 
	 * @param bulkByScrollResponse
	 */
	private void createLogger(BulkByScrollResponse bulkByScrollResponse) {
		long totalNum = bulkByScrollResponse.getTotal();
		long addNum = bulkByScrollResponse.getCreated();
		long updateNum = bulkByScrollResponse.getUpdated();
		long deleteNum = bulkByScrollResponse.getDeleted();
		String message = "No info";
		StringBuffer buffer = new StringBuffer();

		// 判断处理总数
		if (totalNum > 0) {
			buffer.append(StringUtils.isNotEmpty(buffer.toString()) ? ";" : ""
					+ "Handle data number：" + Long.valueOf(totalNum).toString());
		}

		// 判断添加总数
		if (addNum > 0) {
			buffer.append(StringUtils.isNotEmpty(buffer.toString()) ? ";" : ""
					+ "Add data number：" + Long.valueOf(addNum).toString());
		}

		// 判断修改总数
		if (updateNum > 0) {
			buffer.append(StringUtils.isNotEmpty(buffer.toString()) ? ";" : ""
					+ "Update data number："
					+ Long.valueOf(updateNum).toString());
		}

		// 判断删除总数
		if (deleteNum > 0) {
			buffer.append(StringUtils.isNotEmpty(buffer.toString()) ? ";" : ""
					+ "Delete data number："
					+ Long.valueOf(deleteNum).toString());
		}

		logger.info(message);
	}

	/**
	 * 连接ES
	 */
	public void connect() {
		try {
			// 判断是否创建RestHighLevelClient对象
			if (StringUtils.isEmpty(client)) {
				// 设置ES实例的名称
				client = new RestHighLevelClient(
						RestClient.builder(new HttpHost(InetAddress
								.getByName(this.host), this.port)));

				logger.info("Elasticsearch connect success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Elasticsearch connect error");
		}
	}

	/**
	 * 关闭ES
	 */
	public void disconnect() {
		try {
			// 判断是否关闭RestHighLevelClient对象
			if (StringUtils.isNotEmpty(client)) {
				client.close();
				logger.info("Elasticsearch disconnect success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Elasticsearch disconnect error");
		}
	}
}