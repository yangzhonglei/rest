package com.yzl.study.db2rest.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.yzl.study.db2rest.model.ComplexQuery;

@Component
public class ComplexQueryDaoImpl implements ComplexQueryDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<ComplexQuery> selectAll() {
		
		
		String sql = "select * from complex_query_config where status=0";
		SqlParameterSource paramSource = new MapSqlParameterSource();
		return namedParameterJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<ComplexQuery>(ComplexQuery.class));
		
	}
	
	
	
	

}
