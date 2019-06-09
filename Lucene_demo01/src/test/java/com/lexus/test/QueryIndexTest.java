package com.lexus.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;

/**
 * 索引库数据检索
 * Query:封装了搜索条件
 */
public class QueryIndexTest {
    @Test
    public void test01() throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        //创建Query搜索对象
        //QueryParser queryParser = new QueryParser("要搜索的列", 分词器);
        QueryParser queryParser = new QueryParser("desc", analyzer);
        Query query = queryParser.parse("lucene");

        //创建Directory流对象,声明索引库位置
        Directory dic = FSDirectory.open(new File("D:/usr/index").toPath());
        //创建索引读取对象IndexReader
        IndexReader reader = DirectoryReader.open(dic);
        //创建索引搜索对象IndexSearcher
        IndexSearcher searcher = new IndexSearcher(reader);
        //使用索引搜索对象，执行搜索，返回结果集TopDocs
        TopDocs topDocs = searcher.search(query, 2);
        //解析结果集
        ScoreDoc[] docs = topDocs.scoreDocs;
        for (ScoreDoc doc : docs) {
            int index = doc.doc;
            Document document = searcher.doc(index);
            System.out.println(document.get("name"));
            System.out.println(document.get("pic"));
            //System.out.println(document.get("desc"));
            System.out.println(document.get("id"));
            System.out.println(document.get("price"));
        }
        //释放资源
        reader.close();
    }

}
