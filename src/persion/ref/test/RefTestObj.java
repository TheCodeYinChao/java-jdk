package persion.ref.test;

/**
 * description: RefTestObj <br>
 * date: 2020/3/23 15:37 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class RefTestObj {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + "[id=" + this.id + "]";
    }

    @Override
    protected void finalize() {
        System.out.println("Object [" + this.hashCode() + "][ id=" + this.id + "] come into finalize");
        try {
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
