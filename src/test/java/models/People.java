package models;

import lombok.*;

//@Data//содержит полезные аннотации геттеры сеттеры хэшкоды
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Builder//можно создавать объекты с определенными параметрами
public class People {
    private String name;
    private Integer age;
    private String sex;


}
