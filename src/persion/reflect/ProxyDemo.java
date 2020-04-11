package persion.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * description: ProxyDemo <br>
 * date: 2020/3/24 11:28 <br>
 * author: zyc <br>
 * version: 1.0 <br>
 */
public class ProxyDemo implements InvocationHandler{
    private Object object;

    public ProxyDemo(Object object) {
        this.object = object;
    }

    public static void main(String[] args) {
        ProxyobI o = (ProxyobI) Proxy.newProxyInstance(ProxyobJE.class.getClassLoader(),  //这里类型转换异常 ，为什么 ？？ 因为jdk 代理是基于接口来的

                ProxyobJE.class.getInterfaces(), new ProxyDemo(new ProxyobJE()));

        o.print();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("增强");
        method.invoke(object ,args);
        return null;
    }
}
