import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int resumeCount = 0; // количество резюме в хранилище

    void clear() {
        Arrays.fill(storage, 0, resumeCount, null );
        resumeCount = 0;
    }

    void save(Resume r) {
        for (int i = 0; i < resumeCount; i++) {
            if ( storage[i].uuid.equals(r.uuid)) {
                System.out.println("База уже содержит резюме с таким ID");
                return;
            }
        }
        storage[resumeCount] = r;
        resumeCount++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < resumeCount; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    return storage[i];
                }
            }
        System.out.println("Резюме с таким ID отсутствует в базе");
        return null;
    }

    void delete(String uuid) {
        boolean deleted = false;
            for (int i = 0; i < resumeCount; i++) {
                if (storage[i].uuid.equals(uuid)) { // удаляем резюме с нужным uuid
                    storage[i] = null;
                    deleted = true;
                    resumeCount--;
                }
                if (deleted) { //если удалили элемент, то сдвигаем оставшиеся элементы в массиве влево
                    storage[i] = storage[i + 1];
                }
            }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
            Resume[] result = new Resume[resumeCount];
            System.arraycopy(storage, 0, result, 0, resumeCount);
            return result;

    }

    int size() {
        return resumeCount;
    }
}
