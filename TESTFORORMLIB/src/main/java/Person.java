import Annotations.MyColumn;
import Annotations.MyEntity;
import Annotations.MyId;
import lombok.Data;

@Data
@MyEntity("people")
public class Person {
    @MyId
    private long id;
    @MyColumn("name")
    private String name;
    @MyColumn("surname")
    private String surname;
    @MyColumn("age")
    private int age;
    @MyColumn("weight")
    private double weight;

}
