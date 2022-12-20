package icu.cucurbit.note.examples.bytebuddy.records;

public record Record(String a) {

    public Record() {
        this("default a");
    }
}
