package ed.inf.adbs.minibase.base;

import ed.inf.adbs.minibase.Utils;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private Head head;

    private List<Atom> body;

    public Query(Head head, List<Atom> body) {
        this.head = head;
        this.body = body;
    }

    public Head getHead() {
        return head;
    }

    public List<Atom> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return head + " :- " + Utils.join(body, ", ");
    }

    /**
     * Deep Copy for query object, where deep copy of body and head are considered
     * @return return a new query object
     */
    public Query deepcopy()
    {
        Head new_head=this.head.deepcopy();
        List<Atom> termList=new ArrayList<>();
        for(int i=0;i<this.body.size();i++)
        {
            Atom new_term=this.body.get(i).deepcopy();
            termList.add(new_term);
        }
        Head new_obj= this.head.deepcopy();

        return new Query(new_obj,termList);
    }
}
