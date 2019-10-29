package com.maven.common.elasticsearch.entity;

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

	private static final long serialVersionUID = 2750751924889886533L;

	// 搜索对象
	private QueryEntity query;

	// 高亮对象
	private HighLightEntity highLight;

	// 排序对象集合
	private List<SortEntity> sorts = new ArrayList<SortEntity>();

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

	public List<SortEntity> getSorts() {
		return sorts;
	}

	public void setSorts(List<SortEntity> sorts) {
		this.sorts = sorts;
	}
}