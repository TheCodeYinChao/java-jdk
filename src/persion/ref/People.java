package persion.ref;

/**
 * description: People <br>
 * date: 2020/3/19 18:57 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class People {
    public People(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private  String id ;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "People{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
