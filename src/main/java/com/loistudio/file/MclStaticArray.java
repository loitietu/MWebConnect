package com.loistudio.file;

import java.util.ArrayList;

public class MclStaticArray {
    private ArrayList<String> list = new ArrayList<>();
            
    public MclStaticArray(String array) {
        String[] values = array.substring(1, array.length()-1).split(","); 
        for(String value : values) {
            list.add(value.trim());
        }
    }
        
    public MclStaticArray() {}
            
    public void add(String value) {
        list.add(value);
    }
            
    public void remove(String value) {
        list.remove(value);
    }
            
    public int size() {
        return list.size();
    }
            
    public String get(int index) {
        return list.get(index);
    }
            
    @Override
    public String toString() {
        return list.toString();
    }
}