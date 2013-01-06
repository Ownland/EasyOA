package cn.edu.nju.software.db;

public interface DB {
	public static final String DATABASENAME = "contact.db";
	public static final int DATABASE_VERSION = 1;

	public interface TABLES {
		public interface CONTACT {
			public static final String TABLENAME = "tbl_contact";

			public interface FIELDS {
				public static final String ID = "userId";
				public static final String NAME = "name";
				public static final String NAMEPINYIN = "namePinyin";
				public static final String PHONE = "phone";
				public static final String MOBILE = "mobile";
				public static final String EMAIL = "email";
				public static final String DEPARTMENT = "department";
				public static final String NICKNAME = "nickName";
				public static final String ADDRESS = "address";
				public static final String NOTE = "note";
				public static final String GROUPID = "groupId";
			}

			public interface SQL {
				public static final String CREATE = "create table if not exists "
						+ TABLENAME
						+ "("
						+ FIELDS.ID
						+ " integer primary key,"
						+ FIELDS.NAME
						+ " varchar(20),"
						+ FIELDS.NAMEPINYIN
						+ " varchar(20),"
						+ FIELDS.PHONE
						+ " varchar(20),"
						+ FIELDS.MOBILE
						+ " varchar(20),"
						+ FIELDS.EMAIL
						+ " varchar(20),"
						+ FIELDS.DEPARTMENT
						+ " varchar(20),"
						+ FIELDS.NICKNAME
						+ " varchar(20),"
						+ FIELDS.ADDRESS
						+ " varchar(30),"
						+ FIELDS.NOTE
						+ " varchar(40),"
						+ FIELDS.GROUPID + " integer)";
				public static final String DROPTABLE = "drop table if exists "
						+ TABLENAME;
				public static final String INSERT = "insert into "
						+ TABLENAME
						+ "("
						+ FIELDS.ID
						+ ","
						+ FIELDS.NAME
						+ ","
						+ FIELDS.NAMEPINYIN
						+ ","
						+ FIELDS.PHONE
						+ ","
						+ FIELDS.MOBILE
						+ ","
						+ FIELDS.EMAIL
						+ ","
						+ FIELDS.DEPARTMENT
						+ ","
						+ FIELDS.NICKNAME
						+ ","
						+ FIELDS.ADDRESS
						+ ","
						+ FIELDS.NOTE
						+ ","
						+ FIELDS.GROUPID
						+ ") values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
				public static final String UPDATE = "update " + TABLENAME
						+ " set " + FIELDS.NAME + "='%s'," + FIELDS.NAMEPINYIN
						+ "='%s'," + FIELDS.PHONE + "='%s'," + FIELDS.MOBILE
						+ "='%s'," + FIELDS.EMAIL + "='%s',"
						+ FIELDS.DEPARTMENT + "='%s'," + FIELDS.NICKNAME
						+ "='%s'," + FIELDS.ADDRESS + "=%s," + FIELDS.NOTE
						+ "='%s'," + FIELDS.GROUPID + "=%s where " + FIELDS.ID
						+ "=%s";
				public static final String DELETE = "delete from " + TABLENAME
						+ " where userId=%s";
				// 查询的SQL语句
				public static final String SELECT = "select *from " + TABLENAME
						+ " where %s";
				// 改变分组
				public static final String CHANGEGROUP = "update " + TABLENAME
						+ " set " + FIELDS.GROUPID + "=%s where %s";
			}
		}

		public interface GROUP {
			public static final String TABLENAME = "tbl_group";

			public interface FIELDS {
				public static final String GROUPID = "groupId";
				public static final String GROUPNAME = "groupName";
			}

			public interface SQL {
				// 创建表的SQL语句
				public static final String CREATE = "create table if not exists "
						+ TABLENAME
						+ "("
						+ FIELDS.GROUPID
						+ " integer primary key autoincrement,"
						+ FIELDS.GROUPNAME + " varchar(20))";
				// 删除表的SQL语句
				public static final String DROPTABLE = "drop table if exists "
						+ TABLENAME;
				// 插入一条记录的SQL语句
				public static final String INSERT = "insert into " + TABLENAME
						+ "(" + FIELDS.GROUPNAME + ") values('%s')";
				// 根据主键emailId号修改一条记录的SQL语句
				public static final String UPDATE = "update " + TABLENAME
						+ " set " + FIELDS.GROUPNAME + " = '%s' where "
						+ FIELDS.GROUPID + " = '%s' ";
				// 删除一条记录的SQL语句
				public static final String DELETE = "delete from " + TABLENAME
						+ " where groupId=%s";
				// 查询的SQL语句
				public static final String SELECT = "select *from " + TABLENAME
						+ " where %s";
			}
		}
	}
}
