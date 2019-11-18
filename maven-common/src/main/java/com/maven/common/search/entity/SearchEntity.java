package com.maven.common.search.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索实体类
 * 
 * @author chenjian
 * @createDate 2019-10-29
 */
public class SearchEntity implements Serializable {

	private static final long serialVersionUID = 5828691450251705061L;

	// 搜索对象
	private QueryEntity query;

	// 高亮对象
	private HighLightEntity highLight;

	// 排序对象集合(Elastic)
	private List<ElasticSortEntity> elasticSorts = new ArrayList<ElasticSortEntity>();

	// 排序对象集合(Solr)
	private List<SolrSortEntity> solrSorts = new ArrayList<SolrSortEntity>();

	public QueryEntity getQuery() {
		return query;
	}

	public void setQuery(QueryEntity query) {
		this.query = query;
	}

	public HighLightEntity getHighLight() {
		return highLight;
	}

	public void setHighLight(HighLightEntity highLight) {
		this.highLight = highLight;
	}

	public List<ElasticSortEntity> getElasticSorts() {
		return elasticSorts;
	}

	public void setElasticSorts(List<ElasticSortEntity> elasticSorts) {
		this.elasticSorts = elasticSorts;
	}

	public List<SolrSortEntity> getSolrSorts() {
		return solrSorts;
	}

	public void setSolrSorts(List<SolrSortEntity> solrSorts) {
		this.solrSorts = solrSorts;
	}
}