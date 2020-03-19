package persion.ref;

/**
 * description: Bean <br>
 * date: 2020/3/19 17:58 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class Bean {
    private String name;
    private String id ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
