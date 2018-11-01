# chinese-weekdays-sdk
符合中国国情的工作日计算-java-sdk

####本SDK支持SPI

所有SPI都定义在`com.github.streamlang.weekdays.spi` 

#### 使用

1.引入依赖
```xml
<dependency>
    <groupId>com.github.streamlang</groupId>
	<artifactId>chinese-weekdays-sdk</artifactId>
	<version>0.0.3</version>
</dependency>
```

2.WeekdaysUtil提供具体实现方法

3.StatsDateUtil还有一些在时间维度上做统计的小工具
###配置文件
节假日的定义默认由JsonPatternSource读取，配置文件放在resources目录下即可，文件名为 `weekdays_source.json`

