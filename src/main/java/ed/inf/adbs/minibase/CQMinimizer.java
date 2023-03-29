package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;
//import sun.font.TrueTypeFont;

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
    public  static  boolean Isdistinguished(String check_value, Head head)
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
    public static String printBody(Head head, List<Atom> body)
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
    public static List<Atom> deepCopyBody(List<Atom>body_copy)
    {
        List <Atom> deepCopyAtomList=new ArrayList<>();
        for(Atom atom : body_copy)
        {
            RelationalAtom addAtom=(RelationalAtom) atom.deepcopy();
            deepCopyAtomList.add(addAtom);
        }
        return deepCopyAtomList;
    }

    /**
     * This minimization helper is used to minimize the redunant relation atom in a specific body
     * @param body The parameter body is taken as an input output as minimized body
     * @param body_new body_new is another copy of Atom list, which will be never modified
     * @param head Head is used to check if a specific variable is distinguished
     * @return This method will return a minimized atom list
     */
    public static List<Atom> minimizationHelper(List<Atom> body,List<Atom>body_new, Head head)
    {
        for (int  i=0;i<body.size();i++)
        {
            // We will check if the current atom has the single direction homomorphism with the other atoms in the subbody
            RelationalAtom curAtom =(RelationalAtom) body.get(i);
            List<Atom> subBody =subBodyList(i,body);
            boolean checkHomorphism=isHaveHomomorphism(subBody,body,curAtom,head);
            if (!checkHomorphism)
            {
                continue;
            }
            else
            {
                body.remove(curAtom);
                i=0;
            }


        }
        return body;
    }

    /**
     * This method is used to check if the modified body is a subset of the original body. To check if the input body is a subset
     * of original body, each relational atom of the original body will be compared with each relational atom of original body
     * @param body This body is modified when each homomorphism applies to the whole atom list, it will be modified and compared with the original body
     * @param bodyCopy The body_copy parameter is an unmodified List<Atom>, where each relational atom will be compared with that of the modified body to check if it
     * is still a subset.
     * @param head head will be used to check if a specific variable is distinguished
     * @return return a boolean to check if it is a subset
     */

    public static boolean IsSubset( List<Atom> body,List<Atom>bodyCopy,Head head)
    {
        for(int i=0;i<body.size();i++)
        {
            RelationalAtom bodyAtom =(RelationalAtom) body.get(i);
            if (! IsSimilar(bodyAtom,bodyCopy,head))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to check if a target body is contains exactly the same Atom as that of the compared body.
     * This method is necessary when we want to check if the body are reversed when applying a specific homomorphism
     * @param body target body to be compared
     * @param bodyCopy original body
     * @param head head from query
     * @return will retun a boolean
     */
    public static boolean checkSame(List<Atom> body,List<Atom>bodyCopy,Head head)
    {
        int cnt =0 ;
        for(int i=0;i<bodyCopy.size();i++)
        {
            RelationalAtom curAtom =(RelationalAtom) bodyCopy.get(i);
            if(! body.contains(curAtom))
            {
                return false;
            }
        }
        return true;
    }


    /**

     This method takes in three arguments, a list of atoms representing the body of a rule, a copy of the body of the rule, and a head atom of the rule.
     It iterates through each atom in the body list and checks if it is similar to the corresponding atom in the bodyCopy list and the head atom.
     If an atom is not similar, it is returned as a RelationalAtom. If all atoms in the body list are similar, the method returns null.
     @param body A list of atoms representing the body of a rule
     @param bodyCopy A copy of the body of the rule
     @param head A head atom of the rule
     @return A RelationalAtom if an atom in the body is not similar to the corresponding atom in the bodyCopy and head atom, null otherwise.
     */
    public static RelationalAtom checkDifferent( List<Atom> body,List<Atom>bodyCopy,Head head)
    {
        for(int i=0;i<body.size();i++)
        {
            RelationalAtom bodyAtom =(RelationalAtom) body.get(i);
            if (! IsSimilar(bodyAtom,bodyCopy,head))
            {
                return bodyAtom;
            }

        }
        return null;
    }

    /**

     Checks if there exists a homomorphism between two lists of atoms and performs necessary updates.
     The method starts by creating an empty list of homomorphisms. It then iterates through the target body
     to find homomorphisms for each atom in the target body against the deleted atom. These homomorphisms
     are stored in the homo_lst.
     After collecting the homomorphisms, the method creates deep copies of the original body and target body,
     which are referred to as tempBody and tempTarget, respectively. These copies are used to apply the
     homomorphisms found earlier and check if they result in a valid homomorphism.
     For each homomorphism in homo_lst, the method creates two lists: mapFromList and mapToList. These lists
     store the terms that need to be replaced and the corresponding replacement terms, respectively. Then, it
     iterates through tempBody and tempTarget, applying the term replacements (homomorphism) using the setAtom method.
     After applying the homomorphism, the method checks if tempBody is a subset of the original body using the
     IsSubset method. If tempBody is a subset and not the same as the original body, the method returns true,
     indicating a valid homomorphism. If not, it proceeds to check for a sub-homomorphism.
     The checkDifferent method is used to find the first atom that differs between tempBody and original body.
     A new target body (newTargetBody) is created by taking a sublist of tempBody starting from the index of the
     different atom. The method then checks for a sub-homomorphism between newTargetBody and tempBody using a
     recursive call to isHaveHomomorphism. If a sub-homomorphism is found, the method returns true.
     If no valid homomorphism or sub-homomorphism is found, the method resets tempBody to its initial state (deep
     copy of the original body) and continues with the next homomorphism in homo_lst. If no valid homomorphism is
     found after iterating through all the homomorphisms in homo_lst, the method returns false.
     @param targetBody The list of target atoms to be checked against the original body.
     @param originalBody The list of original atoms to be checked for homomorphism.
     @param deletedAtom The relational atom to be deleted from the original body.
     @param head The head of the rule being considered.
     @return True if there is a valid homomorphism, false otherwise.
     */
    public static boolean isHaveHomomorphism(List<Atom> targetBody, List<Atom> originalBody,RelationalAtom deletedAtom,Head head)
    {
        List<HashMap<Term,Term>> homoList =new ArrayList<>();
        for( Atom subAtom : targetBody)
        {
            HashMap<Term,Term> homo = new HashMap<Term,Term>();
            homo=checkHomomorphism(deletedAtom,(RelationalAtom) subAtom ,head);
            if(homo!=null)
            {
                if(!homo.isEmpty())
                {homoList.add(homo);}
            }
        }
//        System.out.println("Homo list:"+homo_lst);
        // System.out.println("Homomorphism list:"+homo_lst);
        // Now, since we have single direction rule, we need to apply them to the whole body, we will have a temp body to change
        // If the temp body is still the subset of the original body, then it means the homomorphism works.
        // 1. check if the homomorphism list is empty, if so, just continue; 2. else, iterate through the list, apply every homomorphism and check if it works
        // Since we only care about one homomorphism, so if the current homomorphism works, just break
        // Deep copy the body for changing terms
        List<Atom> tempBody=deepCopyBody(originalBody);
        List<Atom> tempTarget=deepCopyBody(targetBody);
        for(HashMap<Term,Term> map : homoList)
        {
            // iterate through the current hashmap,
            List<Term> mapFromList =new ArrayList<>();
            List<Term> mapToList =new ArrayList<>();
            for (Map.Entry<Term,Term> homo: map.entrySet())
            {
                mapFromList.add(homo.getKey());
                mapToList.add(homo.getValue());
            }
            // System.out.println("Mapping from list:"+map_from);
            // System.out.println("Mapping to list:"+map_to);
            // Now we should have a list for storing maping from element and a list for maping to element

            // Now iterate through each map_form list and map_to list, User Set_value method to apply homomorphism
            for(int i =0; i< tempBody.size();i++)
            {
                RelationalAtom tempRelationalAtom = (RelationalAtom) tempBody.get(i);
                setAtom(tempRelationalAtom,mapFromList,mapToList);
            }
            for(int i=0;i<tempTarget.size();i++)
            {
                RelationalAtom tempRelationalAtom = (RelationalAtom) tempTarget.get(i);
                setAtom(tempRelationalAtom,mapFromList,mapToList);
            }

            // Now check if the temp_body is a subset of the original body, which means we need make sure each relational atom is the same as that in
            // the original body
//            System.out.println("After homo:"+temp_body);
            boolean check_subset = IsSubset(tempBody,originalBody,head);
//            temp_body=Remove_repeat(temp_body);
            if (check_subset)
            {
                if (checkSame(tempBody,originalBody,head))
                {
                    return false;
                }
                originalBody=deepCopyBody(tempBody);
                return true;
            }
            else
            {
                RelationalAtom differentdAtom = checkDifferent(tempBody,originalBody,head);
                int index=tempBody.indexOf(differentdAtom);
                List<Atom> newTargetBody = subBodyList(index,tempBody);
                boolean checkSubHomomorphism=isHaveHomomorphism(newTargetBody,tempBody,differentdAtom,head);
                if (checkSubHomomorphism)
                {
                    return true;
                }
                else
                {
                    tempBody=deepCopyBody(originalBody);
                }
            }
        }
        return false;
    }

    /**
     * This method is used to apply a specific homomorphism to a corresponding atom.
     * @param targetAtom target relationalAtom to be applyed to a homomorphism such as {x-> 4}
     * @param mapFromList a list of map from varibale
     * @param mapToList a list of map to object
     * @return return a applied Atom
     */
    public static RelationalAtom setAtom (RelationalAtom targetAtom, List<Term> mapFromList,List<Term> mapToList)
    {
        for(int i=0;i< targetAtom.getTerms().size();i++)
        {
            Term curTerm=targetAtom.getTerms().get(i);
            for (int key=0;key<mapFromList.size();key++)
            {
                Term mapFrom =mapFromList.get(key);
                Term mapTo = mapToList.get(key);
                if (curTerm.equals(mapFrom))
                {
                    targetAtom.getTerms().set(i,mapTo);
                }
            }
        }
        return targetAtom;
    }

    /**
     * This method is used to check a target relational atom is in the original body. In this method it will use the overrie equals method of relational atom
     * @param target_atom The target relational atom (it might be applied due to applied homomorphism) is used to check if is contained in the original body
     * @param body_copy This is the original body which is used to check if a specific relationatom is contained it.
     * @param head
     * @return return a boolean to check if the relationsal atom is contained in the body.
     */
    public  static  boolean IsSimilar(RelationalAtom target_atom,List<Atom> body_copy,Head head)
    {
        for(int i=0;i<body_copy.size();i++)
        {
            RelationalAtom bodyRelationalAtom =(RelationalAtom) body_copy.get(i);
            if (bodyRelationalAtom.equals(target_atom))
            {
                return true;
            }
        }
        return false;
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
    public static HashMap<Term,Term> checkHomomorphism(RelationalAtom atom1,RelationalAtom atom2,Head head)
    {
        List<Term> termList1=atom1.getTerms();
        List<Term> termList2=atom2.getTerms();

        // If they do not have the same name or the same size of term list, retun null;
        String name1=atom1.getName();
        String name2=atom2.getName();
        // retun a hash map as a homorphism
        HashMap<Term,Term> result=new HashMap<Term,Term>();
        if(!name1.equals(name2))
        {
            return null;
        }
        if(termList1.size()!=termList2.size())
        {
            return null;
        }
        else
        {
            int cnt=0;
            for(int i=0;i<termList1.size();i++) {
                Term term1 = termList1.get(i);
                Term term2 = termList2.get(i);
                boolean Isconstant_1 = term1 instanceof Constant;
                boolean Isconstant_2 = term2 instanceof Constant;
                boolean Isvariable_1 = term1 instanceof Variable;
                boolean Isvariable_2 = term2 instanceof Variable;
                boolean IsDistinguished_1 = Isdistinguished(term1.toString(), head);
                boolean IsDistinguished_2 = Isdistinguished(term2.toString(), head);


                //Rule 1: if the two terms are both variables that are not distinguished, we follow the single direction rule: from atom1 -> atom2
                if (!IsDistinguished_1 && !IsDistinguished_2 && Isvariable_1 && Isvariable_2)
                {
                    result.put(term1,term2);
                    cnt++;
                }
                // Rule 2 : If one of the terms is constant and the other one is a variable that is not distinguished
                else if (!IsDistinguished_1 && Isvariable_1 && Isconstant_2)
                {
                    result.put(term1,term2);
                    cnt++;
                }

                // Rule 3: If one of the terms is not distinguished variabele and another one is
                else if(!IsDistinguished_1 && Isvariable_1 && IsDistinguished_2)
                {
                    result.put(term1,term2);
                    cnt++;
                }

                else if (Isconstant_1 && Isconstant_2)
                {
                    if (term1.toString().equals(term2.toString()))
                    {
                        cnt++;
                    }
                }
                else if (Isvariable_1 && Isvariable_2)
                {
                    if(term1.toString().equals(term2.toString()))
                    {
                        cnt++;
                    }
                }
            }
            if (cnt==termList1.size())
            {
                return result;
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * After minimization, the final body may contain duplicate relational atom, this method will be used to remove the duplicate relational atom. Hence it
     * will out the final body, which will be used as the final result
     * @param target This is the target body, which will be used to remove the duplicate the relational atom
     * @return return the final body, which has been removed the duplicate relational atom
     */
    public static List<Atom> removeRepeat(List<Atom>target)
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
    public static List<Atom> subBodyList(int index,List<Atom>body)
    {
        List<Atom> temp =new ArrayList<>();
        List<Atom> deepCopyBody = deepCopyBody(body);
        for (int i=0;i<deepCopyBody.size();i++)
        {
            if (i!=index)
            {
                temp.add(deepCopyBody.get(i));
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
            body=minimizationHelper(body,body_new,head);
            body=removeRepeat(body);
            String output=printBody(head,body);
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