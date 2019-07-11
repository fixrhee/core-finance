package com.doku.core.finance.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import com.doku.core.finance.data.Notifications;
import com.doku.core.finance.services.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class NotificationRepository {

	private JdbcTemplate jdbcTemplate;

	public Notifications loadDefaultNotificationByGroupID(Integer req) {
		try {
			Notifications notif = this.jdbcTemplate.queryForObject(
					"select a.id, a.name, a.module_url from notifications a inner join groups b on a.id = b.notification_id where b.id = ?",
					new Object[] { req }, new RowMapper<Notifications>() {
						public Notifications mapRow(ResultSet rs, int rowNum) throws SQLException {
							Notifications notif = new Notifications();
							notif.setId(rs.getInt("id"));
							notif.setName(rs.getString("name"));
							notif.setModuleURL(rs.getString("module_url"));
							return notif;
						}
					});
			return notif;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Notifications> loadNotificationByTransferType(Integer transferTypeID) {
		try {
			List<Notifications> notif = this.jdbcTemplate.query(
					"select a.id, a.transfer_type_id, a.enabled, b.name, b.module_url, a.notification_type from transfer_notifications a inner join notifications b on a.notification_id = b.id where a.transfer_type_id = ? and a.enabled = true;",
					new Object[] { transferTypeID }, new RowMapper<Notifications>() {
						public Notifications mapRow(ResultSet rs, int rowNum) throws SQLException {
							Notifications notif = new Notifications();
							notif.setId(rs.getInt("id"));
							notif.setName(rs.getString("name"));
							notif.setModuleURL(rs.getString("module_url"));
							notif.setNotificationType(rs.getString("notification_type"));
							return notif;
						}
					});
			return notif;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Notifications> loadAllNotification(Integer currentPage, Integer pageSize) {
		try {
			List<Notifications> notif = this.jdbcTemplate.query("select * from notifications order by id LIMIT ?,?",
					new Object[] { currentPage, pageSize }, new RowMapper<Notifications>() {
						public Notifications mapRow(ResultSet rs, int rowNum) throws SQLException {
							Notifications notif = new Notifications();
							notif.setId(rs.getInt("id"));
							notif.setName(rs.getString("name"));
							notif.setModuleURL(rs.getString("module_url"));
							return notif;
						}
					});
			return notif;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Notifications loadNotificationByID(Integer id) {
		try {
			Notifications notif = this.jdbcTemplate.queryForObject("select * from notifications where id = ?",
					new Object[] { id }, new RowMapper<Notifications>() {
						public Notifications mapRow(ResultSet rs, int rowNum) throws SQLException {
							Notifications notif = new Notifications();
							notif.setId(rs.getInt("id"));
							notif.setName(rs.getString("name"));
							notif.setModuleURL(rs.getString("module_url"));
							return notif;
						}
					});
			return notif;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void createNotification(NotificationRequest req) {
		jdbcTemplate.update("insert into notifications (name, module_url) values (?,?)", req.getName(),
				req.getModuleURL());
	}

	public void editNotification(NotificationRequest req) {
		jdbcTemplate.update("update notifications set name = ?, module_url = ? where id = ?", req.getName(),
				req.getModuleURL(), req.getId());
	}

	public void deleteNotification(NotificationRequest req) {
		jdbcTemplate.update("delete from notifications where id = ?", req.getId());
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
