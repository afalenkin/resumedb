package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MainCollections {

    private static final String uuid1 = "UUID_1";
    private static final Resume RESUME_1 = new Resume(uuid1);

    private static final String uuid2 = "UUID_2";
    private static final Resume RESUME_2 = new Resume(uuid2);

    private static final String uuid3 = "UUID_3";
    private static final Resume RESUME_3 = new Resume(uuid3);

    private static final String uuid4 = "UUID_4";
    private static final Resume RESUME_4 = new Resume(uuid4);

    public static void main(String[] args) {
        Collection<Resume> collection = new ArrayList();
        collection.add(RESUME_1);
        collection.add(RESUME_2);
        collection.add(RESUME_3);

        Iterator<Resume> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resume r = iterator.next();
            if (r.getUuid().equals(uuid1)) {
                iterator.remove();
            }
        }
        System.out.println(collection.toString() +"\n");

        Map<String, Resume> resumes = new HashMap<>();
        resumes.put(uuid1, RESUME_1);
        resumes.put(uuid2, RESUME_2);
        resumes.put(uuid3, RESUME_3);

        for (Map.Entry<String, Resume> resume: resumes.entrySet()
             ) {
            System.out.println(resume.getValue());
        }
    }


}
