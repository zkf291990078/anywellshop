package com.anywell.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.anywell.domain.Category;
import com.anywell.domain.Product;
import com.anywell.utils.DataSourceUtils;

public class ProductDao {

	public List<Product> findHotProducts() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit 0,9";
		return queryRunner.query(sql, new BeanListHandler<>(Product.class), 1);
	}

	public List<Product> findNewProducts() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit 0,9";
		return queryRunner.query(sql, new BeanListHandler<>(Product.class));
	}

	public List<Category> findAllCategory() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category";
		return queryRunner.query(sql, new BeanListHandler<>(Category.class));
	}

	public List<Product> findProductsBycid(String cid, int index, int pageCount) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		return queryRunner.query(sql, new BeanListHandler<>(Product.class), cid, index, pageCount);
	}
}
