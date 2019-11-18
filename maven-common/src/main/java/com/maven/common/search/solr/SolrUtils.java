package com.maven.common.search.solr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maven.common.StringUtils;
import com.maven.common.page.Pager;
import com.maven.common.properties.LoadPropertiesUtils;
import com.maven.common.search.entity.HighLightEntity;
import com.maven.common.search.entity.QueryEntity;
import com.maven.common.search.entity.SearchEntity;
import com.maven.common.search.entity.SolrSortEntity;

/**
 * Solr搜索引擎类
 * 
 * @author chenjian
 * @createDate 2019-11-06
 */
public class SolrUtils {

	private static Logger logger = LoggerFactory.getLogger(SolrUtils.class);

	// Solr服务器地址
	private String host;

	// Solr的索引名称
	private String index;

	// Solr默认服务器地址
	private final String DEFAULT_HOST = "http://127.0.0.1:8983/solr";

	// Solr默认索引名称
	private final String DEFAULT_INDEX = "students";

	// HttpSolrClient实例对象
	private HttpSolrClient client = null;

	/**
	 * 构造函数
	 */
	public SolrUtils() {
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param index
	 *            Solr的索引名称
	 */
	public SolrUtils(String index) {
		this.index = index;
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            Solr服务器地址
	 * @param core
	 *            Solr的索引名称
	 */
	public SolrUtils(String host, String index) {
		this.host = host;
		this.index = index;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		LoadPropertiesUtils loadPropertiesUtils = LoadPropertiesUtils
				.getInstance("solr.properties");

		this.host = StringUtils.isNotEmpty(this.host) ? this.host
				: StringUtils.isNotEmpty(loadPropertiesUtils.getKey("host")) ? loadPropertiesUtils
						.getKey("host") : DEFAULT_HOST;
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

				SolrDocument solrDocument = client.getById(this.index, id);
				map = structureData(solrDocument);

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

			SolrQuery solrQuery = createQuerySource(searchEntity, pageNo, size);

			QueryResponse queryResponse = client.query(this.index, solrQuery);
			SolrDocumentList solrDocumentList = queryResponse.getResults();
			Map<String, Map<String, List<String>>> highLightList = queryResponse
					.getHighlighting();

			// 设置分页数据
			pager = new Pager();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			// 判断是否有数据
			if (StringUtils.isNotEmpty(solrDocumentList)
					&& solrDocumentList.size() > 0) {
				for (SolrDocument solrDocument : solrDocumentList) {
					Map<String, Object> map = structureData(solrDocument);

					// 判断是否有高亮
					if (StringUtils.isNotEmpty(highLightList)
							&& highLightList.size() > 0) {
						Map<String, List<String>> highLigt = highLightList
								.get(solrDocument.get("id"));

						Iterator<Entry<String, List<String>>> iterator = highLigt
								.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry<String, List<String>> entry = iterator.next();
							map.put(entry.getKey(), entry.getValue().get(0));
						}
					}

					list.add(map);
				}

				pager.setCurrentPage(pageNo);
				pager.setRowsTotal(Long.valueOf(solrDocumentList.getNumFound())
						.intValue());
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
	 * 添加或修改一条数据
	 * 
	 * @param jsonObject
	 *            数据对象
	 * @return 是否添加成功
	 */
	@SuppressWarnings("unchecked")
	public boolean addAndUpdateSingle(JSONObject jsonObject) {
		return addAndUpdateSingle(JSONObject
				.toJavaObject(jsonObject, Map.class));
	}

	/**
	 * 添加或修改一条数据
	 * 
	 * @param map
	 *            数据对象
	 * @return 是否添加成功
	 */
	public boolean addAndUpdateSingle(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 判断数据对象是否存在
		if (StringUtils.isNotEmpty(map)) {
			list.add(map);
		}

		return addAndUpdateAll(list);
	}

	/**
	 * 添加或修改多条数据
	 * 
	 * @param jsonArray
	 *            数据对象集合
	 * @return 是否添加成功
	 */
	@SuppressWarnings("unchecked")
	public boolean addAndUpdateAll(JSONArray jsonArray) {
		return addAndUpdateAll(JSONArray.toJavaObject(jsonArray, List.class));
	}

	/**
	 * 添加或修改多条数据
	 * 
	 * @param list
	 *            数据对象集合
	 * @return 是否添加成功
	 */
	public boolean addAndUpdateAll(List<Map<String, Object>> list) {
		boolean result = false;

		try {
			String message = "No data";

			// 判断传入参数
			if (StringUtils.isNotEmpty(list) && list.size() > 0) {
				connect();

				List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
				for (Map<String, Object> map : list) {
					Map<String, SolrInputField> fields = new HashMap<String, SolrInputField>();

					Iterator<Entry<String, Object>> iterator = map.entrySet()
							.iterator();
					while (iterator.hasNext()) {
						Entry<String, Object> entry = iterator.next();

						SolrInputField solrInputField = new SolrInputField(
								entry.getKey());
						solrInputField.setValue(entry.getValue());
						fields.put(entry.getKey(), solrInputField);
					}

					SolrInputDocument solrInputDocument = new SolrInputDocument(
							fields);
					docs.add(solrInputDocument);
				}

				client.add(this.index, docs);
				UpdateResponse updateResponse = client.commit(this.index);

				// 判断是否添加或修改成功
				if (updateResponse.getStatus() == 0) {
					result = true;
					message = "Add/Update data success";
				}
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Add/Update data error");
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

				UpdateResponse updateResponse = client.deleteById(Arrays
						.asList(ids));

				// 判断是否删除成功
				if (updateResponse.getStatus() == 0) {
					result = true;
					message = "Delete data success";
				}
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
	 * @param query
	 *            条件对象
	 * @return 是否删除成功
	 */
	public boolean deleteByQuery(String query) {
		boolean result = false;

		try {
			String message = "No data";

			// 判断传入参数
			if (StringUtils.isNotEmpty(query)) {
				connect();

				UpdateResponse updateResponse = client.deleteByQuery(
						this.index, query);

				// 判断是否删除成功
				if (updateResponse.getStatus() == 0) {
					result = true;
					message = "Delete data success";
				}
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
		return deleteByQuery("*:*");
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
	 * @return SolrQuery对象
	 */
	private SolrQuery createQuerySource(SearchEntity searchEntity, int pageNo,
			int size) {
		SolrQuery solrQuery = new SolrQuery();

		// 判断是否有搜索对象
		if (StringUtils.isNotEmpty(searchEntity)) {
			QueryEntity query = searchEntity.getQuery();
			HighLightEntity highLight = searchEntity.getHighLight();
			List<SolrSortEntity> sorts = searchEntity.getSolrSorts();

			// 判断是否有搜索条件
			if (StringUtils.isNotEmpty(query)) {
				// 关键字搜索
				if (StringUtils.isNotEmpty(query.getKeyword())) {
					solrQuery.set("q", "*" + query.getKeyword() + "*");
					solrQuery.set("df", "sex");
				}

				// 性别搜索
				if (StringUtils.isNotEmpty(query.getSex())) {
					solrQuery.addFilterQuery("sex:" + query.getSex());
				}

				// 年龄搜索
				if (query.getMinAge() > 0 && query.getMaxAge() > 0) {
					solrQuery.addFilterQuery("age:[" + query.getMinAge()
							+ " TO " + query.getMaxAge() + "]");
				} else {
					// 最小年龄搜索
					if (query.getMinAge() > 0) {
						solrQuery.addFilterQuery("age:[" + query.getMinAge()
								+ " TO *]");
					}

					// 最大年龄搜索
					if (query.getMaxAge() > 0) {
						solrQuery.addFilterQuery("age:[* TO "
								+ query.getMaxAge() + "]");
					}
				}

				// 住址搜索
				if (StringUtils.isNotEmpty(query.getAddress())) {
					solrQuery.addFilterQuery("address:*" + query.getAddress()
							+ "*");
				}
			}

			// 判断是否设置高亮
			if (StringUtils.isNotEmpty(highLight)) {
				List<String> fields = highLight.getFields();
				String preTags = highLight.getPreTags();
				String postTags = highLight.getPostTags();

				solrQuery.setHighlight(true);

				// 判断是否设置高亮字段
				if (StringUtils.isNotEmpty(fields) && fields.size() > 0) {
					for (String fieldName : fields) {
						solrQuery.addHighlightField(fieldName);
					}
				}

				// 判断是否设置高亮标签
				if (StringUtils.isNotEmpty(preTags)
						&& StringUtils.isNotEmpty(postTags)) {
					solrQuery.setHighlightSimplePre(preTags);
					solrQuery.setHighlightSimplePost(postTags);
				}
			}

			// 判断是否设置排序
			if (StringUtils.isNotEmpty(sorts) && sorts.size() > 0) {
				for (SolrSortEntity solrSortEntity : sorts) {
					solrQuery.addSort(solrSortEntity.getFieldName(),
							solrSortEntity.getSort());
				}
			}
		} else {
			solrQuery.set("q", "*:*");
		}

		// 设置查询起始位置和查询数量
		pageNo = pageNo > 0 ? pageNo : 1;
		int start = (pageNo - 1) * size;
		solrQuery.setStart(start);
		solrQuery.setRows(size);

		return solrQuery;
	}

	/**
	 * 重构数据
	 * 
	 * @param solrDocument
	 *            数据对象
	 * @return 重构数据对象
	 */
	private Map<String, Object> structureData(SolrDocument solrDocument) {
		Map<String, Object> map = null;

		// 判断是否有数据
		if (StringUtils.isNotEmpty(solrDocument)) {
			map = new HashMap<String, Object>();
			Iterator<Entry<String, Object>> iterator = solrDocument.iterator();

			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				map.put(entry.getKey(), entry.getValue());
			}
		}

		return map;
	}

	/**
	 * 连接Solr
	 */
	private void connect() {
		try {
			// 判断是否创建HttpSolrClient对象
			if (StringUtils.isEmpty(client)) {
				// 设置HttpSolrClient实例对象
				client = new HttpSolrClient.Builder(this.host).build();

				logger.info("Solr connect success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Solr connect error");
		}
	}

	/**
	 * 关闭Solr
	 */
	private void disconnect() {
		try {
			// 判断是否关闭HttpSolrClient对象
			if (StringUtils.isNotEmpty(client)) {
				client.close();
				logger.info("Solr disconnect success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Solr disconnect error");
		}
	}
}