package forensic;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {
        int s = StdIn.readInt(); // Read the number of STRs
        STR[] strArray = new STR[s]; // Initialize an array of STR objects
    
        for (int i = 0; i < s; i++) {
            String strName = StdIn.readString(); // Read the STR name
            int occurrences = StdIn.readInt(); // Read the number of occurrences
            strArray[i] = new STR(strName, occurrences); // Create a new STR object and add it to the array
        }
    
        Profile profile = new Profile(strArray); // Create a new Profile object using the STR array
        return profile; // Return the Profile object
    }
    

    public void insertPerson(String name, Profile newProfile) {
        treeRoot = insertRecursively(treeRoot, name, newProfile);
    }
    
    private TreeNode insertRecursively(TreeNode current, String name, Profile newProfile) {
        // Base case: found insertion point
        if (current == null) {
            return new TreeNode(name, newProfile, null, null);
        }
    
        // Determine whether to go left or right
        if (name.compareTo(current.getName()) < 0) {
            // If name is less than current node's name, go left
            current.setLeft(insertRecursively(current.getLeft(), name, newProfile));
        } else if (name.compareTo(current.getName()) > 0) {
            // If name is greater than current node's name, go right
            current.setRight(insertRecursively(current.getRight(), name, newProfile));
        } else {
            // Name is already in the tree, so we don't insert duplicates
            // This case may not be needed if names are guaranteed to be unique,
            // but it's good practice to handle it
        }
    
        return current; // Return the current node without changes
    }
    
    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    public int getMatchingProfileCount(boolean isOfInterest) {
        
        // WRITE YOUR CODE HERE
        return countMatchingProfiles(treeRoot, isOfInterest); // update this line
    }
    private int countMatchingProfiles(TreeNode node, boolean isOfInterest){
        if (node == null){
            return 0; //base case
        }
        int markCount = 0;
        if (node.getProfile().getMarkedStatus() == isOfInterest){
            markCount = 1;
        }
        markCount += countMatchingProfiles(node.getLeft(), isOfInterest); //check left subtree
        markCount+= countMatchingProfiles(node.getRight(), isOfInterest); //check right subtree
        return markCount;
    }
    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
    public void flagProfilesOfInterest() {
        flagProfilesRecursively(treeRoot);
    }
            /**WRITE YOUR CODE HERE
        TreeNode node = new TreeNode();
        while (treeRoot != null){
            if (node.getLeft().equals(node))
        }
        */
    private void flagProfilesRecursively(TreeNode node) {
        if (node == null) {
            return; // base case
        }
    
        // traverse left subtree
        flagProfilesRecursively(node.getLeft());
    
        // process current node's profile
        checkAndMarkProfile(node.getProfile());
    
        // traverse right subtree
        flagProfilesRecursively(node.getRight());
    }
    
    private void checkAndMarkProfile(Profile profile) {
        STR[] strs = profile.getStrs();
        if (strs == null || strs.length == 0) {
            return; // no STRs
        }
    
        int matches = 0;
        String combinedSequence = firstUnknownSequence + secondUnknownSequence;
    
        for (STR str : strs) {
            int occurrencesInProfile = str.getOccurrences();
            int occurrencesInUnknown = numberOfOccurrences(combinedSequence, str.getStrString());
    
            if (occurrencesInProfile == occurrencesInUnknown) {
                matches++;
            }
        }
    
        //determine if at least half of the STRs match
        if (matches >= Math.ceil((double)strs.length / 2)) {
            profile.setInterestStatus(true);
        }
    }
    
    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {

        // WRITE YOUR CODE HERE
        int s = getMatchingProfileCount(false);
        String[] unmarkedProfiles = new String[s];
        int i = 0;
        if (s == 0){
            return new String [0];
        }
        Queue<TreeNode> queue = new Queue<>();
        if (treeRoot != null){
            queue.enqueue(treeRoot);
        }
        while (!queue.isEmpty()){
            TreeNode node = queue.dequeue();
            if (!node.getProfile().getMarkedStatus()){
                unmarkedProfiles[i++] = node.getName();
            }
            if (node.getLeft() != null){
                queue.enqueue(node.getLeft());
            }
            if (node.getRight()!= null){
                queue.enqueue(node.getRight());
            }
        }
        return unmarkedProfiles;
    }

    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    public void removePerson(String fullName) {
        treeRoot = removePersonRecursively(treeRoot, fullName);
    }
    
    private TreeNode removePersonRecursively(TreeNode current, String fullName) {
        if (current == null) {
            return null;
        }
        int compareResult = fullName.compareTo(current.getName());
        if (compareResult < 0) {
            current.setLeft(removePersonRecursively(current.getLeft(), fullName));
        } else if (compareResult > 0) {
            current.setRight(removePersonRecursively(current.getRight(), fullName));
        } else {
            // case 1: no child
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            }
            // case 2: one child
            if (current.getLeft() == null) return current.getRight();
            if (current.getRight() == null) return current.getLeft();
    
            // case 3: two children
            TreeNode successor = findMin(current.getRight());
            current.setName(successor.getName());
            current.setProfile(successor.getProfile());
            current.setRight(removeMin(current.getRight()));
        }
        return current;
    }
    
    private TreeNode findMin(TreeNode node) {
        while (node.getLeft() != null) node = node.getLeft();
        return node;
    }
    
    private TreeNode removeMin(TreeNode node) {
        if (node.getLeft() == null) return node.getRight();
        node.setLeft(removeMin(node.getLeft()));
        return node;
    }
    

    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        String[] unmarkedPeople = getUnmarkedPeople();
        for (String person : unmarkedPeople) {
            removePerson(person);
        }
    }
    

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}
