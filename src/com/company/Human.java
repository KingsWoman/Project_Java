package com.company;
import java.util.ArrayList;
import java.util.List;

public class Human {
    private String name;
    private String last_name;
    private String group;
    private String points_for_topics;
    private List<Theme> points_for_themes;
    private String city = "Не указано";
    private String birthday = "Не указано";
    private String nickname = "Не указано";
    private String photo = "Не указано";
    private String sex = "Не указано";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPoints_for_topics() {
        return points_for_topics;
    }

    public void setPoints_for_topics(String points_for_topics) {
        this.points_for_topics = points_for_topics;
    }

    public List<Theme> getPoints_for_themes() {
        return points_for_themes;
    }

    public void setPoints_for_themes(List<Theme> points_for_themes) {
        this.points_for_themes = points_for_themes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Human() {}

    public Human(String name, String last_name, String group, String points_for_topics, List<Theme> points_for_themes) {
        this.name = name;
        this.last_name = last_name;
        this.group = group;
        this.points_for_topics = points_for_topics;
        this.points_for_themes = points_for_themes;
    }

    void AllInfo() {
        System.out.println("Имя: " + name);
        System.out.println("Фамилия: " + last_name);
        System.out.println("Дата рождения: " + birthday);
        System.out.println("Пол: " + sex);
        System.out.println("Город: " + city);
        System.out.println("Общий бал: " + points_for_topics);
        System.out.println("Никнейм: " + nickname);
        System.out.println("Ссылка на фото: " + photo);
        System.out.println("ТЕМЫ:  ");
        for (int i = 0; i < points_for_themes.size(); i++) {
            System.out.format("    %-80s  %s \n", points_for_themes.get(i).getName(), "Общий бал за тему: " + points_for_themes.get(i).getTotalScore());
        }
        System.out.println("**********************************************************************************************************");
    }
    @Override
    public String toString() {
        return "Human_2{" +
                "name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", group='" + group + '\'' +
                ", points_for_topics='" + points_for_topics + '\'' + "\n" +
                ", points_for_themes=" + points_for_themes +
                '}';
    }

    public static class Theme {
        private String name;
        private int totalScore;
        private final List<Pair> points_for_homeWork;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }

        public List<Pair> getPoints_for_homeWork() {
            return points_for_homeWork;
        }

        public Theme(String name) {
            this.name = name;
            points_for_homeWork = new ArrayList<>();
        }

        public void addHomeWork(String id, Integer mark) {
            points_for_homeWork.add(new Pair(id, mark));
        }

        @Override
        public String toString() {
            return "Theme{" +
                    "name='" + name + '\'' +
                    ", totalScore=" + totalScore +
                    ", points_for_homeWork=" + points_for_homeWork +
                    '}';
        }

        public static class Pair {
            private String key;
            private int mark;

            public Pair(String key, int mark) {
                this.key = key;
                this.mark = mark;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public int getMark() {
                return mark;
            }

            public void setMark(int mark) {
                this.mark = mark;
            }

            @Override
            public String toString() {
                return "Pair{" +
                        "key='" + key + '\'' +
                        ", mark=" + mark +
                        '}';
            }
        }
    }
}
