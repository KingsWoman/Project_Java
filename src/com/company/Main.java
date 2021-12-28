package com.company;

import au.com.bytecode.opencsv.CSVReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws Exception {
        SqlLite.Conn();

        Scanner in = new Scanner(System.in);
        //Подключение файла
        String csvFile = "JavaRTF.csv";
        CSVReader reader = new CSVReader(new FileReader(csvFile), ',');
        List<String[]> stringOfData;
        //Чтение всего файла сразу
        stringOfData = reader.readAll();

        String[] first_line = Arrays.toString(stringOfData.get(0)).replaceAll("[\\[\\]]", "").split(";");
        String[] second_line = Arrays.toString(stringOfData.get(1)).split(";");
        List<Human.Theme> themes = new ArrayList<>();
        String name = "";
        Human.Theme human = null;
        for (int i = 3; i < first_line.length; i++) {
            if (!first_line[i].isEmpty()) {
                if (!name.equals(first_line[i])) {
                    human = new Human.Theme(first_line[i]);
                    themes.add(human);
                }
            } else {
                assert human != null;
                human.addHomeWork(second_line[i], 0);
            }
        }

        SqlLite.CreateDB(themes);

        List<Human> allStudent = new ArrayList<>();

        for (int i = 3; i < stringOfData.size(); i++) {
            String[] result = Arrays.toString(stringOfData.get(i)).replaceAll("[\\[\\]]", "").split(";");
            String first_name = result[0].split(" ")[1];
            String last_name = result[0].split(" ")[0];
            String group = result[1];
            String points_for_topic = result[2];
            List<Human.Theme> humans_theme = new ArrayList<>();

            int j = 4;
            for (int k = 0; k < themes.size(); k++) {
                Human.Theme theme = new Human.Theme(themes.get(k).getName());
                for (int t = 0; t < themes.get(k).getPoints_for_homeWork().size(); t++) {
                    theme.addHomeWork(themes.get(k).getPoints_for_homeWork().get(t).getKey(), Integer.valueOf(result[t + j]));
                }
                theme.setTotalScore(Integer.parseInt(result[j - 1]));
                j = j + themes.get(k).getPoints_for_homeWork().size() + 1;
                humans_theme.add(theme);
            }
            allStudent.add(new Human(first_name, last_name, group, points_for_topic, humans_theme));
        }

        //APi
        int offset = 0;
        int counter = 1000;
        double number_count;
        do {
            String access_token = "3d96b4a485720e4aca2eccc07d3212a0ccc7ba3dd1417f58ba620d4789ab68852e19d02edea0f09db8409";
            String API_URL = "https://api.vk.com/method/groups.getMembers?group_id=198188261&offset=" + offset + "&fields=city,bdate,nickname,photo_max,sex,members_count&access_token=" + access_token + "&v=5.81";

            // создаем URL из строки
            URL url = Url_JsonUtils.createUrl(API_URL);

            // загружаем Json в виде Java строки
            String resultJson = Url_JsonUtils.parseUrl(url);
            //System.out.println("\nПолученный JSON:\n" + resultJson);

            // парсим полученный JSON
            List<JSONObject> s = Url_JsonUtils.parseCurrentAPIJson(resultJson);

            number_count = Url_JsonUtils.parseCountAPIJson(resultJson);

            //Дальше идет проверка фамилии и имени, если идет совпадение из класса Student и из Vk.api,
            // то происходит добавление информации про этого человека уже в класс Student
            //Затем вся информация попадает прямиков в БД
            for (JSONObject jsonObject : s) {
                for (int k = 0; k < allStudent.size(); k++) {
                    if (allStudent.get(k).getLast_name().equals(jsonObject.get("last_name")) && allStudent.get(k).getName().equals(jsonObject.get("first_name")) ||
                            allStudent.get(k).getLast_name().equals(jsonObject.get("first_name")) && allStudent.get(k).getName().equals(jsonObject.get("last_name"))) {
                        try {
                            JSONObject city = (JSONObject) s.get(0).get("city");
                            allStudent.get(k).setCity((String) city.get("title"));
                        } catch (JSONException ignored) {
                        }
                        //Дата рождения
                        try {
                            allStudent.get(k).setBirthday((String) jsonObject.get("bdate"));
                        } catch (JSONException ignored) {
                        }
                        //Никнейм
                        try {
                            allStudent.get(k).setNickname((String) jsonObject.get("nickname"));
                        } catch (JSONException ignored) {
                        }
                        //Ссылка на фото
                        try {
                            allStudent.get(k).setPhoto((String) jsonObject.get("photo_max"));
                        } catch (JSONException ignored) {
                        }

                        try {
                            allStudent.get(k).setSex(String.valueOf(jsonObject.get("sex")).equals("1") ? "Женский" : "Мужской");
                        } catch (JSONException ignored) {
                        }

                        String[] balls = new String[allStudent.get(k).getPoints_for_themes().size()];

                        for (int j = 0; j < allStudent.get(k).getPoints_for_themes().size(); j++) {
                            balls[j] = String.valueOf(allStudent.get(k).getPoints_for_themes().get(j).getTotalScore());
                        }

                        SqlLite.WriteDB(allStudent.get(k).getName(), allStudent.get(k).getLast_name(), allStudent.get(k).getNickname(),
                                allStudent.get(k).getCity(), allStudent.get(k).getBirthday(), allStudent.get(k).getSex(),
                                  allStudent.get(k).getPhoto(), allStudent.get(k).getPoints_for_topics(), balls, themes);
                    }
                }
            }
            offset += counter;
            Thread.sleep(1000);
        }while(offset < number_count);

        List<Human> base = SqlLite.ReadDB(themes);
        SqlLite.CloseDB();

        while (true) {
            System.out.println("Введите Фамилию и имя");
            String[] name_fal = in.nextLine().split(" ");

            for (int i = 0; i < base.size(); i++) {
                if (base.get(i).getLast_name().equals(name_fal[0]) && base.get(i).getName().equals(name_fal[1])) {
                    base.get(i).AllInfo();
                }
            }
            System.out.println("Если вы хотите выйти с поиска введите break");
            String line = in.nextLine();
            if (line.equals("break")) {
                break;
            }
        }

        System.out.println("Если вы хотите посмотреть весь список введите all");
        String allinfo = in.nextLine();
        if (allinfo.equals("all")) {
            for (int i = 0; i < base.size(); i++) {
                base.get(i).AllInfo();
            }
        }

        new DiagramWindow(base);
    }
}



