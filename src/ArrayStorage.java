import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int resumeCount = 0; // индекс последнего резюме в хранилище

    void clear() {
        Arrays.fill(storage, null);
        resumeCount = 0;
    }

    void save(Resume r) {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                storage[i] = r;
                resumeCount = i;
                return;
            }
        }
    }

    Resume get(String uuid) {
        if (resumeCount > 0) {
            for (int i = 0; i < resumeCount + 1; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    return storage[i];
                }
            }
        }
        return null;
    }

    void delete(String uuid) {
        if (resumeCount > 0) {
            boolean deleted = false;
            for (int i = 0; i < resumeCount + 1; i++) {
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

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        if (resumeCount > 0) {
            Resume[] result = new Resume[resumeCount + 1];
            System.arraycopy(storage, 0, result, 0, resumeCount + 1);
            return result;
        } else {
            return new Resume[0];
        }
    }

    int size() {
        if (resumeCount > 0) {
            return (resumeCount + 1);
        }
        return 0;
    }
}
