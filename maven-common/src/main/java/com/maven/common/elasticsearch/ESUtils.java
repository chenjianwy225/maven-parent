package com.maven.common.elasticsearch;

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
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
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
	private int port;

	// ES的Index名称
	private String index;

	// ES默认服务器地址
	private static final String DEFAULT_HOST = "localhost";

	// ES默认服务器端口
	private static final int DEFAULT_PORT = 9200;

	// ES默认Index名称
	private static final String DEFAULT_INDEX = "studentindex";

	// ESUtils实例类
	private static ESUtils esUtils = null;

	// RestHighLevelClient实例类
	private RestHighLevelClient client = null;

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            地址
	 * @param port
	 *            端口
	 * @param index
	 *            索引名
	 */
	public ESUtils(String host, int port, String index) {
		this.host = host;
		this.port = port;
		this.index = index;
	}

	/**
	 * 实例化
	 * 
	 * @return
	 */
	public static ESUtils getInstance() {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_INDEX);
	}

	/**
	 * 实例化
	 * 
	 * @param host
	 *            地址
	 * @param port
	 *            端口
	 * @return
	 */
	public static ESUtils getInstance(String host, int port) {
		return getInstance(host, port, DEFAULT_INDEX);
	}

	/**
	 * 实例化
	 * 
	 * @param index
	 *            索引名
	 * @return
	 */
	public static ESUtils getInstance(String index) {
		return getInstance(DEFAULT_HOST, DEFAULT_PORT, index);
	}

	/**
	 * 实例化
	 * 
	 * @param host
	 *            地址
	 * @param port
	 *            端口
	 * @param index
	 *            索引名
	 * @return
	 */
	public static ESUtils getInstance(String host, int port, String index) {
		if (StringUtils.isEmpty(esUtils)) {
			esUtils = new ESUtils(host, port, index);
		}

		return esUtils;
	}

	/**
	 * 根据ID获取信息
	 * 
	 * @param id
	 *            ID编号
	 * @return
	 */
	public Map<String, Object> findById(String id) {
		Map<String, Object> map = null;

		try {
			connect();

			GetRequest getRequest = new GetRequest(this.index, id);
			GetResponse response = client.get(getRequest,
					RequestOptions.DEFAULT);
			map = response.getSourceAsMap();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (StringUtils.isNotEmpty(client)) {
				disconnect();
			}
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
	 * @return
	 */
	public Pager findPager(SearchEntity searchEntity, int pageNo, int size) {
		Pager pager = null;

		try {
			connect();

			SearchRequest searchRequest = new SearchRequest(this.index);
			SearchSourceBuilder searchSourceBuilder = createQuerySource(
					searchEntity, pageNo, size);
			searchRequest.source(searchSourceBuilder);

			SearchResponse response = client.search(searchRequest,
					RequestOptions.DEFAULT);
			SearchHits searchHits = response.getHits();

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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (StringUtils.isNotEmpty(client)) {
				disconnect();
			}
		}

		return pager;
	}

	/**
	 * 添加一条信息
	 * 
	 * @param jsonObject
	 *            数据对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int addSingle(JSONObject jsonObject) {
		return addSingle(JSONObject.toJavaObject(jsonObject, Map.class));
	}

	/**
	 * 添加一条信息
	 * 
	 * @param map
	 *            数据对象
	 * @return
	 */
	public int addSingle(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 判断数据对象是否存在
		if (StringUtils.isNotEmpty(map)) {
			list.add(map);
		}

		return addAll(list);
	}

	/**
	 * 添加多条信息
	 * 
	 * @param jsonArray
	 *            数据对象集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int addAll(JSONArray jsonArray) {
		return addAll(JSONArray.toJavaObject(jsonArray, List.class));
	}

	/**
	 * 添加多条信息
	 * 
	 * @param list
	 *            数据对象集合
	 * @return
	 */
	public int addAll(List<Map<String, Object>> list) {
		int status = -1;

		try {
			connect();

			// 判断数据对象是否存在
			if (StringUtils.isNotEmpty(list) && list.size() > 0) {
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
				status = bulkResponse.status().getStatus();

				logger.info("Add succee");
			} else {
				logger.info("Not data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Add error");
		} finally {
			if (StringUtils.isNotEmpty(client)) {
				disconnect();
			}
		}

		return status;
	}

	/**
	 * 修改一条信息
	 * 
	 * @param jsonObject
	 *            数据对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int updateSingle(JSONObject jsonObject) {
		return updateSingle(JSONObject.toJavaObject(jsonObject, Map.class));
	}

	/**
	 * 修改一条信息
	 * 
	 * @param map
	 *            数据对象
	 * @return
	 */
	public int updateSingle(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 判断数据对象是否存在
		if (StringUtils.isNotEmpty(map)) {
			list.add(map);
		}

		return updateAll(list);
	}

	/**
	 * 修改多条信息
	 * 
	 * @param jsonArray
	 *            数据对象集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int updateAll(JSONArray jsonArray) {
		return updateAll(JSONArray.toJavaObject(jsonArray, List.class));
	}

	/**
	 * 修改多条信息
	 * 
	 * @param list
	 *            数据对象集合
	 * @return
	 */
	public int updateAll(List<Map<String, Object>> list) {
		int status = -1;

		try {
			connect();

			// 判断数据对象是否存在
			if (StringUtils.isNotEmpty(list) && list.size() > 0) {
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
				status = bulkResponse.status().getStatus();

				logger.info("Update success");
			} else {
				logger.info("Not data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update error");
		} finally {
			if (StringUtils.isNotEmpty(client)) {
				disconnect();
			}
		}

		return status;
	}

	/**
	 * 删除一个或多个信息
	 * 
	 * @param ids
	 *            ID编号集合
	 * @return
	 */
	public int deleteAll(String... ids) {
		int status = -1;

		try {
			connect();

			// 判断数据对象是否存在
			if (StringUtils.isNotEmpty(ids) && ids.length > 0) {
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
				status = bulkResponse.status().getStatus();

				logger.info("Delete success");
			} else {
				logger.info("Not data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete error");
		} finally {
			if (StringUtils.isNotEmpty(client)) {
				disconnect();
			}
		}

		return status;
	}

	/**
	 * 删除索引所有信息
	 */
	public void deleteIndex() {
		try {
			connect();

			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(
					this.index);
			client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

			logger.error("Delete index success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete index error");
		} finally {
			if (StringUtils.isNotEmpty(client)) {
				disconnect();
			}
		}
	}

	/**
	 * 生成查询信息
	 * 
	 * @param searchEntity
	 *            查询对象
	 * @param pageNo
	 *            当前页数
	 * @param size
	 *            每页数量
	 * @return
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
					WildcardQueryBuilder sexWQB = QueryBuilders.wildcardQuery(
							"sex", query.getSex());
					boolQueryBuilder.must(sexWQB);
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
					MatchQueryBuilder addressMQB = QueryBuilders.matchQuery(
							"address", query.getAddress());
					boolQueryBuilder.must(addressMQB);
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
		if (pageNo <= 0) {
			pageNo = 1;
		}
		int start = (pageNo - 1) * size;
		searchSourceBuilder.from(start).size(size);

		return searchSourceBuilder;
	}

	/**
	 * 生成日志信息
	 * 
	 * @param bulkResponse
	 */
	private void createLogger(BulkResponse bulkResponse) {
		int addNum = 0, updateNum = 0, deleteNum = 0;
		StringBuffer loggerInfo = new StringBuffer();

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

		// 判断是否有新增
		if (addNum > 0) {
			loggerInfo.append("新增数据总数：" + Integer.valueOf(addNum).toString());
		}

		// 判断是否有新增
		if (updateNum > 0) {
			loggerInfo
					.append("更新数据总数：" + Integer.valueOf(updateNum).toString());
		}

		// 判断是否有新增
		if (deleteNum > 0) {
			loggerInfo
					.append("删除数据总数：" + Integer.valueOf(deleteNum).toString());
		}

		// 判断是否有日志信息
		if (StringUtils.isNotEmpty(loggerInfo)) {
			logger.info(loggerInfo.toString());
		}
	}

	/**
	 * 连接ES
	 */
	public void connect() {
		try {
			if (StringUtils.isEmpty(client)) {
				// 设置ES实例的名称
				client = new RestHighLevelClient(
						RestClient.builder(new HttpHost(this.host, this.port,
								"http")));

				logger.info("Connect success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Connect error");
		}
	}

	/**
	 * 关闭ES
	 */
	public void disconnect() {
		try {
			if (StringUtils.isNotEmpty(client)) {
				client.close();
				logger.info("Disconnect success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Disconnect error");
		}
	}
}