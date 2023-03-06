package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;
import sun.font.TrueTypeFont;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.io.FileReader;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Minimization of conjunctive queries
 *
 */
public class CQMinimizer {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: CQMinimizer input_file output_file");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];

        minimizeCQ(inputFile, outputFile);

        parsingExample(inputFile);
    }



//    check if a RelationAtom object is contained in the body

    public  static  boolean is_distinguished(String check_value, Head head)
    {
         List<Variable> temp = head.getVariables();
         for(int i=0;i<temp.size();i++)
         {
             if (temp.get(i).toString().equals(check_value))
             {
                 return true;
             }
         }
         return false;
    }

    public static String Minimized(Head head, List<Atom> body)
    {

        String result= head + " :- ";
        for(int i=0;i<body.size();i++)
        {
            if (i==0)
            {
                result = result + body.get(i);
                continue;
            }
            else
            {
                result=result + ", " + body.get(i);
            }
        }
        return result;
    }
    public static List<Atom> deepCopy_body(List<Atom>body_copy)
    {
        List <Atom> new_lst=new ArrayList<>();
        for(Atom atom : body_copy)
        {
            RelationalAtom add_atom=(RelationalAtom) atom.deepcopy();
            new_lst.add(add_atom);
        }
        return new_lst;
    }


    public static boolean Is_subset( List<Atom> body,List<Atom>body_copy,Head head)
    {
        for(int i=0;i<body.size();i++)
        {
            RelationalAtom body_atom =(RelationalAtom) body.get(i);
            if (! Is_similar(body_atom,body_copy,head))
            {
                return false;
            }
        }
        return true;
    }
    public static List<Atom> Set_body(List<Atom> body, String target, Term set_value)
    {
        for (int i =0; i<body.size();i++)
        {
            RelationalAtom cur_atom = (RelationalAtom) body.get(i);
            List<String> check_contain_term = Is_contain_term(target,cur_atom);
            if(check_contain_term.get(0).equals("true"))
            {
                int index=Integer.parseInt(check_contain_term.get(1));
                ((RelationalAtom) body.get(i)).getTerms().set(index,set_value);
            }
        }
        List<Atom> new_body= new ArrayList<>();
        new_body=body;
        return new_body;
    }


    public  static  boolean Is_similar(RelationalAtom target_atom,List<Atom> body_copy,Head head)
    {
        for(int i=0;i<body_copy.size();i++)
        {
            RelationalAtom body_relation_atom =(RelationalAtom) body_copy.get(i);
            if (body_relation_atom.equals(target_atom))
            {
                return true;
            }
        }
        return false;
    }
    public static List<String> Is_contain_term(String term , RelationalAtom relationalAtom)
    {
        for(int i=0;i<relationalAtom.getTerms().size();i++)
        {
            if (relationalAtom.getTerms().get(i).toString().equals(term))
            {
                List<String> check=new ArrayList<>();
                check.add("true");
                check.add(Integer.toString(i));
                return check;
            }
        }

        List<String> check=new ArrayList<>();
        check.add("false");
        check.add(Integer.toString(Integer.MAX_VALUE));
        return check;
    }

    public static HashMap<String,List<Term>> merge_homo(List<HashMap<Term,Term>> homo)
    {
        HashSet<String> key_set=new HashSet<String>();
        // a hashset for stroing all of the keys
        for (int i=0;i<homo.size();i++)
        {
            for (Map.Entry<Term, Term> set :
                    homo.get(i).entrySet())
            {
                key_set.add(set.getKey().toString());
            }

        }
        Iterator<String> it = key_set.iterator();
        HashMap<String,List<Term>> merged= new HashMap<String,List<Term>>();
        while(it.hasNext())
        {
            String key=it.next();
            List<Term> term_lst=new ArrayList<>();
            for(int i=0;i<homo.size();i++)
            {
                HashMap<Term,Term> cur_map=homo.get(i);
                for (Map.Entry<Term, Term> set :
                        cur_map.entrySet())
                {
                    String cur_key=set.getKey().toString();
                    if (cur_key.equals(key))
                    {
                       term_lst.add(set.getValue());
                    }

                }
            }
            term_lst=remove_duplicate(term_lst);
            merged.put(key,term_lst);
        }
        return merged;
    }

    // Method for checking if there is a homorphism between two relationalAtoms: only for one direction=> atom1 -> atom2
    public static HashMap<Term,Term> check_homomorphism(RelationalAtom atom1,RelationalAtom atom2,Head head)
    {
        List<Term> term_lst_1=atom1.getTerms();
        List<Term> term_lst_2=atom2.getTerms();

        // If they do not have the same name or the same size of term list, retun null;
        String name1=atom1.getName();
        String name2=atom2.getName();
        // retun a hash map as a homorphism
        HashMap<Term,Term> result=new HashMap<Term,Term>();
        if(!name1.equals(name2))
        {
            return null;
        }
        if(term_lst_1.size()!=term_lst_2.size())
        {
            return null;
        }
        else
        {
            for(int i=0;i<term_lst_1.size();i++)
            {
                Term term1=term_lst_1.get(i);
                Term term2=term_lst_2.get(i);
                boolean Isconstant_1 = term1 instanceof Constant;
                boolean Isconstant_2 = term2 instanceof Constant;
                boolean Isvariable_1 = term1 instanceof Variable;
                boolean Isvariable_2 = term2 instanceof Variable;
                boolean IsDistinguished_1= is_distinguished(term1.toString(),head);
                boolean IsDistinguished_2= is_distinguished(term2.toString(),head);


                //Rule 1: if the two terms are both variables that are not distinguished, we follow the single direction rule: from atom1 -> atom2
                if ( ! IsDistinguished_1 && !IsDistinguished_2 && Isvariable_1 && Isvariable_2)
                {
                    if (!term1.toString().equals(term2.toString()))
                    {
                        result.put(term1,term2);
                    }
                }
                // Rule 2 : If one of the terms is constant and the other one is a variable that is not distinguished
                else if (!IsDistinguished_1 && Isvariable_1 && Isconstant_2)
                {
                    result.put(term1,term2);
                }

                // Rule 3: If one of the terms is not distinguished variabele and another one is
                else if(!IsDistinguished_1 && Isvariable_1 && IsDistinguished_2)
                {
                    result.put(term1,term2);
                }

            }
            return result;
        }
    }

    public static List<Term> remove_duplicate(List<Term>lst)
    {
         List<String> temp =new ArrayList<>();
         List<Integer> removed_index=new ArrayList<>();
         for(int i=0;i<lst.size();i++)
         {
             if (!temp.contains(lst.get(i).toString()))
             {
                 temp.add(lst.get(i).toString());
             }
             else
             {
                 removed_index.add(i);
             }
         }
         List<Term> new_lst=new ArrayList<>();
         for(int i =0;i<lst.size();i++)
         {
             if (!removed_index.contains(i))
             {
                 Term new_term= lst.get(i);
                 new_lst.add(new_term);
             }
         }
         return new_lst;
    }

    public static List<Atom> Remove_repeat(List<Atom>target)
    {
        List<Atom> lst=new ArrayList<>();
        for(int i=0;i<target.size();i++)
        {
            RelationalAtom atom = (RelationalAtom) target.get(i);
            boolean find=false;
            if (lst.size()==0)
            {
                lst.add(atom);
                continue;
            }
            for(int j=0;j<lst.size();j++)
            {
                RelationalAtom check_atom = (RelationalAtom) lst.get(j);
                if(check_atom.equals(atom))
                {
                    find=true;
                }
            }
            if(!find)
            {
                lst.add(atom);
            }
        }
        return lst;
    }

    // this method will be used to generate the subList of Atoms, which contains all the atoms apart from an atom of specific list
    public static List<Atom> Sub_body_lst(int index,List<Atom>body)
    {
        List<Atom> temp =new ArrayList<>();
        List<Atom> deepcopy = deepCopy_body(body);
        for (int i=0;i<deepcopy.size();i++)
        {
            if (i!=index)
            {
                temp.add(deepcopy.get(i));
            }
        }
        return temp;
    }
    /**
     * CQ minimization procedure
     *
     * Assume the body of the query from inputFile has no comparison atoms
     * but could potentially have constants in its relational atoms.
     *
     */
    public static void minimizeCQ(String inputFile, String outputFile) {
        // TODO: add your implementation
        File new_file= new File(inputFile);
        try
        {
            Query query = QueryParser.parse(Paths.get(inputFile));
            Query query_new = QueryParser.parse(Paths.get(inputFile));
            Head head = query.getHead();
            List<Atom> body = query.getBody();
            List<Atom> body_new=query_new.getBody();
            List<HashMap<Term,Term>>  homomorphism = new ArrayList<>();
            // Scan the body,and check if the current atom has homomorphism with the other atoms.
            for (int  i=0;i<body.size();i++)
            {
                // We will check if the current atom has the single direction homomorphism with the other atoms in the subbody
                RelationalAtom cur_atom =(RelationalAtom) body.get(i);
                List<Atom> sub_body =Sub_body_lst(i,body);
//                System.out.println("Current atom:"+cur_atom);
//                System.out.println("Sub body:"+sub_body);
                List<HashMap<Term,Term>> homo_lst =new ArrayList<>();
                for( Atom sub_atom : sub_body)
                {
                    HashMap<Term,Term> homo = new HashMap<Term,Term>();
                    homo=check_homomorphism(cur_atom,(RelationalAtom) sub_atom ,head);
                    if(homo!=null)
                    {
                        if(!homo.isEmpty())
                        {homo_lst.add(homo);}
                    }
                }
//                System.out.println("Homomorphism list:"+homo_lst);
                // Now, since we have single direction rule, we need to apply them to the whole body, we will have a temp body to change
                // If the temp body is still the subset of the original body, then it means the homorphism works.
                // 1. check if the homomorphism list is empty, if so, just continue; 2. else, iterate through the list, apply every homorphism and check if it works
                // Since we only care about one homorphism, so if the current homorphism works, just break

                // Deep copy the body for changing terms
                List<Atom> temp_body=deepCopy_body(body);
                for(HashMap<Term,Term> map : homo_lst)
                {
                    // iterate through the current hashmap,
                    List<Term> map_from_lst =new ArrayList<>();
                    List<Term> map_to_lst =new ArrayList<>();
                    for (Map.Entry<Term,Term> homo: map.entrySet())
                    {
                        map_from_lst.add(homo.getKey());
                        map_to_lst.add(homo.getValue());
                    }
//                    System.out.println("Mapping from list:"+map_from);
//                    System.out.println("Mapping to list:"+map_to);
                    // Now we should have a list for storing maping from element and a list for maping to element

                    // Now iterate through each map_form list and map_to list, User Set_value method to apply homomorphism
                    for(int key=0; key<map_from_lst.size();key++)
                    {
                        Term map_from=map_from_lst.get(key);
                        Term map_to =map_to_lst.get(key);
                        Set_body(temp_body,map_from.toString(),map_to);
                    }
                    // Now check if the temp_body is a subset of the original body, which means we need make sure each relational atom is the same as that in
                    // the original body
                    boolean check_subset = Is_subset(temp_body,body_new,head);
                    if (check_subset)
                    {
//                        System.out.println("After Apply homorphism:"+ temp_body);
                        body=deepCopy_body(temp_body);
                        break;
                    }
                    else
                    {
//                        System.out.println("Not work, temp body is :"+ temp_body);
//                        System.out.println("Not work, currrent body is:"+ body);
                        temp_body=deepCopy_body(body);
                    }
                }
//                System.out.println("\n");
            }
            // remove_repeat
            body=Remove_repeat(body);
            String output=Minimized(head,body);
            System.out.println(output);
            // print
            HashMap<Term,Term> map=new HashMap<Term,Term>();
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile,false));
            writer.write(output);
            writer.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Example method for getting started with the parser.
     * Reads CQ from a file and prints it to screen, then extracts Head and Body
     * from the query and prints them to screen.
     */

    public static void parsingExample(String filename) {

        try {
            Query query = QueryParser.parse(Paths.get(filename));
            // Query query = QueryParser.parse("Q(x, y) :- R(x, z), S(y, z, w)");
            // Query query = QueryParser.parse("Q(x) :- R(x, 'z'), S(4, z, w)");

            System.out.println("Entire query: " + query);
            Head head = query.getHead();
            System.out.println("Head: " + head);
            List<Atom> body = query.getBody();
            System.out.println("Body: " + body);
        }
        catch (Exception e)
        {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

}
