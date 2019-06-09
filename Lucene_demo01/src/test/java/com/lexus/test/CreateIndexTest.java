package com.lexus.test;

import com.lexus.dao.BookDao;
import com.lexus.dao.impl.BookDaoImpl;
import com.lexus.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建索引库
 */
public class CreateIndexTest {
    private BookDao bookDao = new BookDaoImpl();

    @Test
    public void test01() throws IOException {
        //1)查询数据
        Book book = new Book();
        book.setId(1);
        book.setDesc("LS&ES");
        book.setName("Lexus");
        book.setPic("www.lexus.com");
        book.setPrice(999f);
        Document doc = new Document();
        //类似添加一个列
        doc.add(new TextField("id", book.getId().toString(), Field.Store.YES));
        doc.add(new TextField("desc", book.getDesc(), Field.Store.YES));
        doc.add(new TextField("name", book.getName(), Field.Store.YES));
        doc.add(new TextField("pic", book.getPic(), Field.Store.YES));
        doc.add(new TextField("price", book.getPrice().toString(), Field.Store.YES));

        //2)将查询数据存储到索引库中
        //Analyzer 分词器
        Analyzer analyzer = new StandardAnalyzer();
        //Directory d 索引数据存储的位置
        Directory d = FSDirectory.open(new File("D:/usr/index").toPath());
        //IndexWriterConfig conf 作用就是配置写索引的优化参数
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

        //IndexWriter作用:管理索引数据,增删改查等
        IndexWriter indexWriter = new IndexWriter(d, iwc);
        indexWriter.addDocument(doc);

        //关闭资源
        indexWriter.close();
    }

    @Test
    public void test02() throws Exception {
        // 采集数据
        List<Book> list = bookDao.queryBookList();
        // 创建Document文档对象
        List<Document> documents = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Book book : list) {
                Document doc = new Document();
                doc.add(new TextField("id", book.getId().toString(), Field.Store.YES));
                doc.add(new TextField("desc", book.getDesc(), Field.Store.YES));
                doc.add(new TextField("name", book.getName(), Field.Store.YES));
                doc.add(new TextField("pic", book.getPic(), Field.Store.YES));
                doc.add(new TextField("price", book.getPrice().toString(), Field.Store.YES));
                //将单个对象到集合中
                documents.add(doc);
            }
        }
        // 创建分析器（分词器）
        Analyzer analyzer = new StandardAnalyzer();
        // 创建IndexWriterConfig配置信息类
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 创建Directory对象，声明索引库存储位置
        Directory dir = FSDirectory.open(new File("D:/usr/index").toPath());
        // 创建IndexWriter写入对象
        IndexWriter writer = new IndexWriter(dir, config);
        // 把Document写入到索引库中
        writer.addDocuments(documents);
        // 释放资源
        writer.close();
    }


}
