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


    /**
     * This method is used to check if the current variable is from the head,which means it is a distinguished variable
     * @param check_value the value, which will be checked if it is a distinguished variable
     * @param head The head element will be used to display the distinguished variable list to compare
     * @return return a boolean to check
     */
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

    /**
     * This method will be used to print the minmized body
     * @param head the parameter head will be print combined with body
     * @param body the parameter body will be print combined with head
     * @return this method will return a boolean,which can check if the current variable is distinguished.
     */
    public static String Print_Body(Head head, List<Atom> body)
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

    /**
     * This method is used to deepcopy the body object, which is a List<Term>. And since, for each Term object, it has a deepcopy method
     * It will simply iterate through the body list, and add the atom to the a new Body list
     * @param body_copy
     * @return It will return a new body list
     */
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

    /**
     * This minimization helper is used to minimize the redunant relation atom in a specific body
     * @param body The parameter body is taken as an input output as minimized body
     * @param body_new body_new is another copy of Atom list, which will be never modified
     * @param head Head is used to check if a specific variable is distinguished
     * @return This method will return a minimized atom list
     */
    public static List<Atom> minimization_helper(List<Atom> body,List<Atom>body_new, Head head)
    {
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
        body=Remove_repeat(body);
        return body;
    }


    /**
     * This method is used to check if the modified body is a subset of the original body. To check if the input body is a subset
     * of original body, each relational atom of the original body will be compared with each relational atom of original body
     * @param body This body is modified when each homomorphism applies to the whole atom list, it will be modified and compared with the original body
     * @param body_copy The body_copy parameter is an unmodified List<Atom>, where each relational atom will be compared with that of the modified body to check if it
     * is still a subset.
     * @param head head will be used to check if a specific variable is distinguished
     * @return return a boolean to check if it is a subset
     */

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

    /**
     * This method is used to modifiy the target body, if a specific homomorphism is applied. For example if the original body is: R(x,y), R(x,3).
     * If a homomorphism y -> 3 applies to this body, then the target body will be modified to R(x,3), R(x,3)
     * @param body Input a body, which will be modified
     * @param target The target is the map from term. In the previous case the target is y
     * @param set_value The set_value is the map to term. In the previous case the map to element is 3
     * @return return a modified body
     */
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


    /**
     * This method is used to check a target relational atom is in the original body. In this method it will use the overrie equals method of relational atom
     * @param target_atom The target relational atom (it might be applied due to applied homomorphism) is used to check if is contained in the original body
     * @param body_copy This is the original body which is used to check if a specific relationatom is contained it.
     * @param head
     * @return return a boolean to check if the relationsal atom is contained in the body.
     */
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


    /**
     * This method is used to check if a specific term is contained in a specific relational atom
     * @param term This term might be modified due to an applied homomorphism, and it will be checked if contained in a target relational atom
     * @param relationalAtom The relationatom will be iterated to check if one of the terms is the same as the target term
     * @return It will return a list of strings, which contain two elements one for checking if it is contained, another for the index of the contained term
     * for example, ["true",1] means that it is contained in the relational atom and the contained position is 1.
     */
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

    /**
     * This method is used to check if there is a single directional homomorphism between two specific relational atoms.
     * For example, R1=(x,y) R2=(x,3), there is a homomorphis: y->3.
     * @param atom1 This is a relational atom for starting point o f homomorphism.
     * @param atom2 This is a realtional atom
     * @param head head might be used to check distinguished value
     * @return it will return a hashmap, which contains the homomorphis for eaxampel {y=3} means y -> 3
     */
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

    /**
     * After minimization, the final body may contain duplicate relational atom, this method will be used to remove the duplicate relational atom. Hence it
     * will out the fianal body, which will be used as the final result
     * @param target This is the target body, which will be used to remove the duplicate the relational atom
     * @return return the final body, which has been removed the duplicate relational atom
     */
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

    /**
     * This method is used to generate sub body. For example:If we have a body: [R(x,y),R(x,3),R(a,b)], if Sub_body_lst(1,body)
     * It will generate the sub_body which contains the other relational atoms apart fromm the relational atom of index 1: Sub_body:[R(x,y),R(a,b)]
     * @param index The index is used to store a relational atom of a specific index, and the generated sub_body will contain the other relation altoms
     * @param body Original body, which is used to generate the sub body
     * @return return the sub_body
     */
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
            RelationalAtom R1= (RelationalAtom) body.get(0);
            RelationalAtom R2 = (RelationalAtom) body.get(1);
            List<Term> term1= R1.getTerms();
            List<Term> term2=R2.getTerms();
            // Use the Minimization helper to minimize the relational atoms from the body
            body=minimization_helper(body,body_new,head);
            String output=Print_Body(head,body);
            System.out.println(output);
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
//             Query query = QueryParser.parse("Q(x, y) :- R(x, z), S(y, z, w)");
            // Query query = QueryParser.parse("Q(x) :- R(x, 'z'), S(4, z, w)");

        }
        catch (Exception e)
        {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

}
