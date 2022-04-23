package org.lab5;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

public class normalUser {
    SqlSession session;

    normalUser() throws IOException {
        //1.读取mybatis的核心配置文件(mybatis-config.xml)
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");

        //2.通过配置信息获取一个SqlSessionFactory工厂对象
        SqlSessionFactory fac = new SqlSessionFactoryBuilder().build(in);
        //3.通过工厂获取一个SqlSession对象
        session = fac.openSession();
    }

    private static final Logger logger = LoggerFactory.getLogger(normalUser.class);

    public void borrowBook() {
        logger.info("""
                 please enter 1 or 2 . input 9 will exit\s
                1 show books\s
                2 borrow book
                """
        );
        String str = null;
        BufferedReader databf = new BufferedReader(new InputStreamReader(System.in));
        while ((str = databf.readLine()) != null) {
            int com = Integer.parseInt(str);
            // find Records cardnum ->   isbn -> book. info
            if (com == 1) {
                String cardnum = databf.readLine();
                List<Record> recordlist = session.selectOne("recordMapper.showRecords", cardnum);
                for (Record r : recordlist) {
                    Book book = session.selectOne("BookMapper.showBooks", r.getISBN());
                    System.out.print(book);
                }
            } else {
                String isdn = databf.readLine();
                Book book = session.selectOne("BookMapper.showBooks", isdn);
                if (book.getInventory() > 0){
                    session.update("")
                }
            }
        }
    }

    public void returnBook() throws IOException {

    }

    public void queryBook() throws IOException {
        System.out.print("""
                 please enter 8 parameters . input 9 will exit\s
                1 category\s
                2 title\s
                3 publisher\s
                4 pub year1\s
                5 pub year2\s
                6 author\s
                7 price1\s
                8 price2\s
                """
        );
        String str = null;
        BufferedReader databf = new BufferedReader(new InputStreamReader(System.in));
        logger.info("please enter attribute");
        String sortattr = databf.readLine();
        while ((str = databf.readLine()) != null) {
            String[] temp = str.split(",");
            //（书号，类别，书名，出版社，年份，作者，价格，数量）
            String ISBN = temp[0].trim().equals("") ? null : temp[0].trim();
            String Category = temp[1].trim().equals("") ? null : temp[1].trim();
            String Title = temp[2].trim().equals("") ? null : temp[2].trim();
            String Publisher = temp[3].trim().equals("") ? null : temp[3].trim();
            Integer Year = Integer.parseInt(temp[4].trim());
            Integer Year2 = Integer.parseInt(temp[5].trim());
            String Author = temp[6].trim().equals("") ? null : temp[6].trim();
            Double Price = temp[7].trim().equals("") ? null : Double.parseDouble(temp[7].trim());
            Double Price2 = Double.parseDouble(temp[8].trim());
            queryCondition query = new queryCondition(ISBN, Category, Title, Publisher, Year, Author, Price, Price2, Year2);
            List<Book> list = session.selectList("bookMapper.findBooks", query);
            switch (sortattr) {
                case "Category":
                    list.sort(Comparator.comparing(Book::getCategory));
                    break;
                case "Title":
                    list.sort(Comparator.comparing(Book::getTitle));
                    break;
                case "Year":
                    list.sort(Comparator.comparing(Book::getYear));
                    break;
                case "Author":
                    list.sort(Comparator.comparing(Book::getAuthor));
                    break;
                case "Publisher":
                    list.sort(Comparator.comparing(Book::getPublisher));
                    break;
                case "Price":
                    list.sort(Comparator.comparing(Book::getPrice));
                    break;
                case "Total":
                    list.sort(Comparator.comparing(Book::getTotal));
                    break;
                case "Inventory":
                    list.sort(Comparator.comparing(Book::getInventory));
                    break;
            }
            for (Book B : list) {
                System.out.println(B);
            }
        }
    }
}
