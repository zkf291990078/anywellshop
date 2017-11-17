package com.anywell.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.anywell.domain.Category;
import com.anywell.domain.Order;
import com.anywell.domain.OrderItem;
import com.anywell.domain.Product;
import com.anywell.utils.DataSourceUtils;
import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

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

	public int getCount(String cid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid=? ";
		Long count = (Long) queryRunner.query(sql, new ScalarHandler(), cid);
		return count.intValue();
	}

	public Product findProductById(String pid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=? ";
		return queryRunner.query(sql, new BeanHandler<>(Product.class), pid);
	}

	public void saveOrderItems(Order order) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtils.getConnection();
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orderItem values (?,?,?,?,?)";
		for (OrderItem item : order.getOrderItems()) {
			queryRunner.update(conn, sql, item.getItemid(), item.getCount(), item.getSubtotal(),
					item.getProduct().getPid(), item.getOrder().getOid());
		}
	}

	public void saveOrder(Order order) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtils.getConnection();
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orders values (?,?,?,?,?,?,?,?)";
		queryRunner.update(conn, sql, order.getOid(), order.getOrdertime(), order.getTotal(), order.getState(),
				order.getAddress(), order.getName(), order.getTelephone(), order.getUser().getUid());
	}

	public void updateOrderAdrr(Order order) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set address=?,telephone=?,name=? where oid=? ";
		queryRunner.update(sql, order.getAddress(), order.getTelephone(), order.getName(), order.getOid());
	}

	public void updateOrderState(String r6_Order) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set state=? where oid=?";
		runner.update(sql, 1, r6_Order);
	}

	public List<Order> findAllOrders(String uid, int currentPage, int currentCont) throws SQLException {
		// TODO Auto-generated method stub
		int index = (currentPage - 1) * currentCont;
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid=? limit ?,?";
		return queryRunner.query(sql, new BeanListHandler<Order>(Order.class), uid, index, currentCont);
	}

	public int getOrdersCount() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from orders ";
		Long count = (Long) queryRunner.query(sql, new ScalarHandler());
		return count.intValue();
	}

	public List<Map<String, Object>> findAllOrderItems(String oid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orderitem o,product p where o.pid=p.pid and oid=? ";
		return queryRunner.query(sql, new MapListHandler(), oid);
	}
}
