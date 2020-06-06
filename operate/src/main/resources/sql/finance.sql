DROP TABLE IF EXISTS ashare_history;
CREATE TABLE ashare_history (
  id int(11) NOT NULL AUTO_INCREMENT,
  code varchar(10) DEFAULT NULL COMMENT '代码',
  alias varchar(10) DEFAULT NULL COMMENT '代码(带字母)',
  date date DEFAULT NULL COMMENT '日期',
  open decimal(8,3) DEFAULT NULL COMMENT '开盘价',
  close decimal(8,3) DEFAULT NULL COMMENT '收盘价',
  high decimal(8,3) DEFAULT NULL COMMENT '最高',
  low decimal(8,3) DEFAULT NULL COMMENT '最低',
  volume decimal(20,3) DEFAULT NULL COMMENT '交易量',
  psy_type varchar(10) DEFAULT NULL COMMENT 'psy类型',
  psy decimal(5,2) DEFAULT NULL COMMENT 'psy值',
  psyma decimal(5,2) DEFAULT NULL COMMENT 'psyma值',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY AK_code_date_unique_key (code,date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='股票历史数据';

DROP TABLE IF EXISTS ashare_list;
CREATE TABLE ashare_list (
  id int(11) NOT NULL AUTO_INCREMENT,
  code varchar(10) DEFAULT NULL,
  alias varchar(10) DEFAULT NULL,
  name varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='股票表';

DROP TABLE IF EXISTS ashare_transaction;
CREATE TABLE ashare_transaction (
  id int(11) NOT NULL AUTO_INCREMENT,
  code varchar(10) DEFAULT NULL,
  date date DEFAULT NULL,
  type tinyint(4) DEFAULT NULL COMMENT '1-买入；2-卖出',
  price decimal(8,3) DEFAULT NULL,
  win_day int(11) DEFAULT NULL COMMENT '买入后第几天开始盈利',
  create_time datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='股票交易记录表';

DROP TABLE IF EXISTS ashare_recommend;
CREATE TABLE ashare_recommend (
  id int(11) NOT NULL AUTO_INCREMENT,
  code varchar(10) DEFAULT NULL,
  date date DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ashare_recommend_uk_001(code,date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='股票推荐表';

