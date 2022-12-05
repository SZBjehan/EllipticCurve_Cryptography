import java.util.*;

public class EllipticCurve {


    public static void main(String[]args){

        Scanner key = new Scanner(System.in); //
        System.out.println("Let the Elliptic Curve be : y^2 = x^3 + ax + b mod r ");
//        ArrayList<int[]> pList = new ArrayList<>();

        System.out.println("a value?");
        int a= key.nextInt();
        System.out.println("b value?");
        int b= key.nextInt();
        System.out.println("r value?");
        int r= key.nextInt();

        System.out.println("x1 value of P?");
        int x1P= key.nextInt();
        System.out.println("y1 value of P?");
        int y1P= key.nextInt();
        System.out.println("The value of P is ["+x1P+","+y1P+"]");


        Map<String, int[]> valueP = new HashMap<>();
        getAllP(a,b,r,x1P,y1P, valueP);

        System.out.println("a value of first person?");
        int a1= key.nextInt();
        int[] A = valueP.get(a1+"P");
        System.out.println("Therefore, first person sends : "+Arrays.toString(A));
        System.out.println(" ");

        System.out.println("a value of second person?");
        int b1= key.nextInt();
        int[] B = valueP.get(b1+"P");
        System.out.println("Therefore, second person sends : "+Arrays.toString(B));
        System.out.println(" ");

        Map<String, int[]> valueFirstPersonP = new HashMap<>();
        Map<String, int[]> valueSecondPersonP = new HashMap<>();
        getAllP(a,b,r,B[0],B[1], valueFirstPersonP);
        int[] aKey=valueFirstPersonP.get(a1+"P");
        System.out.println("Therefore, from "+a1+Arrays.toString(B) + " we get "+ Arrays.toString(aKey));
        System.out.println(" ");

        getAllP(a,b,r,A[0],A[1], valueSecondPersonP);
        int[] bKey=valueSecondPersonP.get(b1+"P");
        System.out.println("Therefore, from "+b1+Arrays.toString(A) + " we get "+ Arrays.toString(bKey));

        System.out.println(" ");

        if (aKey[0] == bKey[0] ){
            System.out.println("Shared Key = "+aKey[0]);
        }


//        for(Map.Entry mp: valueP.entrySet()){
//            System.out.println(mp.getKey()+ " "+ Arrays.toString((int[]) mp.getValue()));
//        }


    }

    private static void getAllP(int a, int b, int r, int x1P, int y1P, Map<String, int[]> valueP) {
        int x2Q=x1P;
        int y2Q=y1P;


        int []k = {x1P,y1P};

        System.out.println("The equation is :  y^2 = x^3 + ("+ a +" * x ) + ("+ b +" ) mod " + r );
        System.out.println("Points on the Elliptic Curve : ");

        valueP.put("P", k);
        System.out.println("P = "+ Arrays.toString(k));

        for(int i=2; i<30; i++){
//            boolean CheckDoubling= CheckDoubling(a, b, r, x1P, y1P);
            int [] nextP;
            if (i==2){
                nextP= NextPValueDoubling(a,b,r,x1P,y1P);

            }else {
                nextP= NextPValueAddition(a,b,r,x1P,y1P,x2Q,y2Q);
            }

            x1P=nextP[0];
            y1P=nextP[1];

            valueP.put(i+"P", nextP);
            System.out.println(i+"P = "+Arrays.toString(nextP));


            if(nextP[0]==x2Q){
                if(Math.ceilMod(nextP[1],r) == (y2Q*-1)) {
                    System.out.println(i+1 + "P = " + "Point of Infinity");
                    break;
                }
            }
        }
        System.out.println("  ");

    }


    private static int[] NextPValueAddition(int a, int b, int r, int x1P, int y1P, int x2Q, int y2Q) {
//        int s1= Math.ceilMod((y2Q-y1P) , r );
        int s1= (y2Q-y1P);

        int s2= (x2Q-x1P);

        if(s1<0 && s2<0){
            s1=s1*(-1);
            s2=s2*(-1);
        } else if (s2<0 && s1>=0) {
            s1=s1*(-1);
            s2=s2*(-1);
        }
        s1= Mod( s1, r );
//        System.out.print("s1 = "+ s1 + " ");

        s2= modInverse(s2, r );
//        System.out.println("s2 = "+ s2 + " ");

        int s= (s1*s2) % r;
//        System.out.print("s = "+ s + " ");

        return nextP(r, x1P, y1P, x2Q, s);
    }
    private static int[] NextPValueDoubling(int a, int b, int r, int x1P, int y1P) {

        int s1= Mod(((3 * (x1P * x1P)) + a) , r );
        int s2= modInverse((2 * y1P), r );
        int s= (s1*s2) % r;

        int x2Q=x1P;
        return nextP(r, x1P, y1P, x2Q, s);
    }
    private static int[] nextP(int r, int x1P, int y1P,int x2Q, int s) {
        int x3 = Mod(((s*s) - x1P - x2Q) , r);
        int y3= Mod(((s * (x1P - x3)) - y1P), r);

        int [] nextP= {x3,y3};
        return nextP;
    }



    static int Mod(int A, int B){
        int ans = A % B;
        if(ans<0){
            ans=B+ans;
        }
        return ans;
    }
    static int modInverse(int A, int M) {
        for (int X = 1; X < M; X++)
            if (((A % M) * (X % M)) % M == 1)
                return X;
        return 1;
    }

    private static boolean CheckDoubling(int a, int b, int r, int x1P, int y1P) {

        int LHS = Mod((y1P*y1P), r);
        int RHS = Mod( ((x1P*x1P*x1P) + (a*x1P) + b ) ,r);

        if (LHS==RHS){
            return true;
        }
        return false;
    }

}
