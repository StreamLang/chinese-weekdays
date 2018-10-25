# chinese-weekdays-sdk
符合中国国情的工作日计算-java-sdk

####本SDK支持SPI

所有SPI都在`com.github.streamlang.weekdays.spi` 下


#### 使用

1.调用WeekdaysDataBuilder.build() 完成工具的初始化。

2.WeekdaysUtil提供具体实现方法

###配置文件
节假日的定义默认由JsonPatternSource读取，配置文件放在resources目录下即可，文件名为 `weekdays_source.json`

