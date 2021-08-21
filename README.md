# java-web-scraping
Web Scraping of Java

- wikipediaからリンク抽出してツリー作成
- キーワード重複は@を付与
- 未探索は$を付与
- キーワードは上位優先
- 語、学は探索せず$付与
- 探索は1秒以上間隔あける

※`<div class="mw-parser-output">`直下で最初の`<p>`タグ対象

※リンク先がwikiで始まる

※hrefの方をキーワードにする


## 環境

Java 11.0.9
PC   Mac

## 実行方法

workディレクトリ配下

### ビルド

```
mvn install
```

### 実行

```
java -jar ./target/work-1.0-jar-with-dependencies.jar キーワード
```

例　下記の場合は、30秒くらいかかります
```
java -jar ./target/work-1.0-jar-with-dependencies.jar 物流
```
