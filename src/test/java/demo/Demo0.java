package demo;

import org.noear.esearchx.EsContext;
import org.noear.esearchx.EsGlobal;

/**
 * @author noear 2022/11/21 created
 */
public class Demo0 {
    EsContext context;

    public void timeout(){
        context.indice("").timeout(12);
    }

    /*public void timeout2(){
        EsGlobal.onCommandBefore(holder->{
            holder.get
        });
    }*/
}
