# chinese-weekdays-Scraper 
 
从网上爬取节假日具体安排，并转换为sdk所需要的格式

## 数据来源

聚合数据->万年历 `https://www.juhe.cn/docs/api/id/177`
-> 获取近期假期

## 使用
执行com.github.streamlang.scraper.Main 

4个参数：

1. 聚合数据万年历api的appKey

2. 起始年份

3. 终止年份

4. json文件路径（可不填，缺省为classes目录下weekdays_source-***.json）

