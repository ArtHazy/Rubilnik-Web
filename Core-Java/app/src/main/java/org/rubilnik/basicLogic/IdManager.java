package org.rubilnik.basicLogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;



public class IdManager {
    private IdNode root;
    private int idLength;
    private List<Character> idChars;

    public IdManager(int idLength, List<Character> idChars) {
        this.idChars = idChars != null ? idChars : Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        this.root = new IdNode(null);
        this.idLength = idLength;
    }

    class IdNode {
        IdNode parent;
        boolean isLeaf = false;
        List<IdNode> children;
        Character val;
        List<Character> freeChildValues = new LinkedList<>(idChars); // for removing from [0]
    
        public IdNode(Character val) {
            this.parent = null;
            this.children = new ArrayList<>();
            this.val = val;
        }
        boolean isBusy(){
            return this.children.size() == idChars.size() || isLeaf;
        }
        boolean isEmpty(){
            return this.children.size() == 0;
        }
        IdNode createChild() throws RuntimeException{
            if (freeChildValues.size()<1) throw new RuntimeException("Unable to create child. Node is busy");
            var newChild = new IdNode(freeChildValues.remove(0));
            var index = idChars.indexOf(newChild.val);
            if (index<=this.children.size()) this.children.add(index,newChild);
            else this.children.add(newChild);
            newChild.parent = this;
            return newChild;
        }
        void removeChild(IdNode child){
            var index = idChars.indexOf(child.val);
            if (index <= freeChildValues.size()) this.freeChildValues.add(index, child.val);
            else this.freeChildValues.add(child.val);
            this.children.remove(child);
            child.parent = null;
        }
    }

    private void checkIdString(String idString) throws IllegalArgumentException {
        if (idString == null || idString.length() != idLength) throw new IllegalArgumentException("ID doesn't match requirements");
        char[] idArray = idString.toCharArray();    
        for (Character ch : idArray) {
            if (!idChars.contains(ch)) throw new IllegalArgumentException("ID contains prohibited values");
        }        
    }

    public String getFreeId() throws RuntimeException {
        var idBuilder = new StringBuilder();
        boolean isFound = _visitNodeForGet(root, idBuilder);
        if (isFound) return idBuilder.toString();
        else throw new RuntimeException("Could not find free id");
    }

    private boolean _visitNodeForGet(IdNode node, StringBuilder idBuilder) {
        if (node.isBusy()) return false;
        if (node!=root) idBuilder.append(node.val);
        if (idBuilder.length() == idLength) {
            node.isLeaf = true;
            return true;
        };
        if (node.isEmpty()) {
            return _visitNodeForGet(node.createChild(), idBuilder);
        } else {
            for (IdNode child : node.children){
                var isFound = _visitNodeForGet(child, idBuilder);
                if (isFound) return true;
            }
            return _visitNodeForGet(node.createChild(), idBuilder);
        }
    }

    public void deleteId(String id) throws IllegalArgumentException{
        checkIdString(id); // throws
        var idArray = id.toCharArray();
        var visited = root;
        boolean flag;
        for (char c : idArray){
            flag = false;
            for (IdNode child : visited.children){
                if (child.val == c) {
                    flag = true;
                    visited = child;
                    break;
                }
            }
            if (flag == false) throw new IllegalArgumentException("Invalid id string provided: "+id);
        }
        var leaf = visited;
        leaf.parent.removeChild(leaf);
    }

    public void log() {
        _visualizeNode(root, 0);
    }

    private void _visualizeNode(IdNode node, int depth) {
        String indentation = "  ".repeat(depth);
        System.out.println(indentation + (node.val == null ? "Root" : node.val));

        for (IdNode child : node.children) {
            _visualizeNode(child, depth + 1);
        }
    }
    public static void main(String[] args) {
        IdManager tree = new IdManager(3, Arrays.asList('A', 'B', 'C'));
        System.out.println(tree.getFreeId());
        System.out.println(tree.getFreeId());
        System.out.println(tree.getFreeId());
        System.out.println(tree.getFreeId());
        System.out.println(tree.getFreeId());
        System.out.println(tree.getFreeId());
        tree.deleteId("ABB");
        tree.deleteId("ABA");
        System.out.println(tree.getFreeId());
        System.out.println(tree.getFreeId());
        tree.log();
    }
}

