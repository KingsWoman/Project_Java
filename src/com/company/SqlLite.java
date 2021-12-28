package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlLite {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void Conn() throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:TEST1.s3db");

        System.out.println("База Подключена!");
    }

    // --------Создание таблицы--------
    public static void CreateDB(List<Human.Theme> themes) throws SQLException {
        statmt = conn.createStatement();
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'Имя' VARCHAR(45) NOT NULL," +
                "'Фамилия' VARCHAR(45) NOT NULL," +
                "'Никнейм' VARCHAR(45)," +
                "'Город' VARCHAR(45)," +
                "'Дата рождения' VARCHAR(30)," +
                "'Пол' VARCHAR(15)," +
                "'Ссылка на фото' text," +
                "'Общий бал' INTEGER,");
        for (int i = 0; i < themes.size(); i++) {
            query.append("'" + themes.get(i).getName() + "' INTEGER,");
        }
        query.deleteCharAt(query.length() - 1).append(");");

        statmt.execute(String.valueOf(query));
        System.out.println("Таблица создана или уже существует.");
    }

    // --------Заполнение таблицы--------
    public static void WriteDB(String name, String last_name, String nickname, String city, String birthday, String sex,
                               String photo, String points_for_topics, String[] balls, List<Human.Theme> themes) throws SQLException {

        statmt.execute("DELETE FROM 'users' WHERE Имя = '" + name + "' AND Фамилия = '" + last_name + "';");
        StringBuilder query = new StringBuilder("INSERT INTO 'users' ('Имя', 'Фамилия', 'Никнейм', 'Город', 'Дата рождения', 'Пол', 'Ссылка на фото', 'Общий бал'");

        for (int i = 0; i < themes.size(); i++) {
            query.append(", '" + themes.get(i).getName() + "'");
        }
        query.append(") VALUES (" +
                "'" + name + "'," +
                "'" + last_name + "'," +
                "'" + nickname + "'," +
                "'" + city + "'," +
                "'" + birthday + "'," +
                "'" + sex + "'," +
                "'" + photo + "'," +
                "'" + points_for_topics + "',");
        for (int i = 0; i < balls.length; i++) {
            query.append("" +
                    "'" +  balls[i] + "',");
        }
        query.deleteCharAt(query.length() - 1).append(");");

        statmt.execute(query.toString());
    }

    // -------- Вывод таблицы--------
    public static List<Human> ReadDB(List<Human.Theme> themes) throws SQLException {
        resSet = statmt.executeQuery("SELECT * FROM users");
        List<Human> result = new ArrayList<>();
        while(resSet.next()) {
            String  name = resSet.getString("Имя");
            String  last_name = resSet.getString("Фамилия");
            String  nickname = resSet.getString("Никнейм");
            String  city = resSet.getString("Город");
            String  birthday = resSet.getString("Дата рождения");
            String  sex = resSet.getString("Пол");
            String  photo = resSet.getString("Ссылка на фото");
            String  points_for_topics = String.valueOf(resSet.getInt("Общий бал"));
            List<Human.Theme> themes_1 = new ArrayList<>();


            Human human = new Human();
            for (int i = 0; i < themes.size(); i++) {
                Human.Theme theme = new Human.Theme(themes.get(i).getName());
                theme.setTotalScore(resSet.getInt(themes.get(i).getName()));
                themes_1.add(theme);
            }
            human.setName(name);
            human.setLast_name(last_name);
            human.setNickname(nickname);
            human.setCity(city);
            human.setBirthday(birthday);
            human.setSex(sex);
            human.setPhoto(photo);
            human.setPoints_for_topics(points_for_topics);
            human.setPoints_for_themes(themes_1);
            result.add(human);
        }


        return result;
    }

    // --------Закрытие--------
    public static void CloseDB() throws SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        //System.out.println("Соединения закрыты");
    }

}

