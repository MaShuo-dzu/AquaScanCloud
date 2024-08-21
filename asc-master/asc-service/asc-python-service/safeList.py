import threading


class ThreadSafeList:
    def __init__(self):
        self._list = []
        self._lock = threading.Lock()

    def append(self, item):
        with self._lock:
            self._list.append(item)

    def remove(self, item):
        with self._lock:
            self._list.remove(item)

    def get(self, index):
        with self._lock:
            return self._list[index]

    def __len__(self):
        with self._lock:
            return len(self._list)

    def __str__(self):
        with self._lock:
            return str(self._list)

    def insert(self, index, item):
        with self._lock:
            self._list.insert(index, item)

    def pop(self, index=None):
        with self._lock:
            if index is None:
                return self._list.pop()
            else:
                return self._list.pop(index)

    def clear(self):
        with self._lock:
            self._list.clear()

    def extend(self, iterable):
        with self._lock:
            self._list.extend(iterable)

    def index(self, item):
        with self._lock:
            return self._list.index(item)

    def count(self, item):
        with self._lock:
            return self._list.count(item)

    def reverse(self):
        with self._lock:
            self._list.reverse()

    def sort(self, *, key=None, reverse=False):
        with self._lock:
            self._list.sort(key=key, reverse=reverse)

    def __getitem__(self, index):
        with self._lock:
            return self._list[index]

    def __setitem__(self, index, value):
        with self._lock:
            self._list[index] = value

    def __delitem__(self, index):
        with self._lock:
            del self._list[index]
