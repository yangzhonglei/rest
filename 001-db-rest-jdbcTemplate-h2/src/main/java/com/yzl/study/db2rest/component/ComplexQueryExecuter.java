package com.yzl.study.db2rest.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.yzl.study.db2rest.model.ComplexQuery;
import com.yzl.study.db2rest.model.Order;
import com.yzl.study.db2rest.model.Page;
import com.yzl.study.db2rest.model.Sort;
import com.yzl.study.db2rest.utils.FieldNameConvertor;

@Component
public class ComplexQueryExecuter {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Integer count(ComplexQuery c, Map<String, Object> paraMap) {
		try {
			String sql = c.getSql();
			String countSql = "";
			if (sql.toLowerCase().contains("from")) {

				int i = sql.toLowerCase().indexOf("from");
				countSql = " select count(1) " + sql.substring(i);

			}
			return namedParameterJdbcTemplate.queryForObject(countSql, paraMap, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Map<String, Object>> query(ComplexQuery c, Map<String, Object> paraMap, Page page) {

		try {
			int pageNo = page.getPage();
			int pageSize = page.getSize();
			String sql = c.getSql();
			StringBuilder sb = new StringBuilder();
			sb.append(sql);
			Sort st = page.getSort();
			if (st != null && st.getOrders() != null && st.getOrders().size() > 0) {

				StringBuilder orderByStr = new StringBuilder();
				for (Order o : st.getOrders()) {
					orderByStr.append(o.getColName() + " " + o.getSortType() + ",");
				}
				orderByStr.deleteCharAt(orderByStr.length() - 1);
				sb.append(" order by " + orderByStr.toString());
			}
			sb.append(" limit " + ((pageNo - 1) * pageSize) + " , " + pageNo * pageSize);
			System.out.println("======================" + sb.toString());
			List<Map<String, Object>> queryForList = namedParameterJdbcTemplate.queryForList(sb.toString(), paraMap);
			return FieldNameConvertor.columnName2FieldName(queryForList);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
