public class BinarySearch {
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int a[] = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i-1] = Integer.parseInt(args[i]);
        }
        System.out.println(binsearchRecur(a,x,-1,a.length));
    }

    //pre: a[i+1] <= a[i]
    //post: R = min(i) : a[i] <= x 
    public static int binsearchIter(int[] a, int x) {
        int l=-1, r=a.length, m=0;
        //inv: a[l]>x && a[r]<=x
        while (l < r - 1) {
            m = l + (r - l) / 2;
            if (a[m] > x) {
                l = m;
            } else {
                r = m;
            }
        }
        return r;
    }

    //pre: a[i+1] <= a[i], l = [-1; a.length], r = [-1; a.length]  
    //post: R = min(i) : a[i] <= x 
    public static int binsearchRecur(int[] a, int x, int l, int r) {
        int m = l + (r - l) / 2;
        if(l >= r - 1) {
            return r;
        } else if(a[m] > x) {
            return binsearchRecur(a, x, m, r);
        } else {
            return binsearchRecur(a, x, l, m);
        }
    }
}