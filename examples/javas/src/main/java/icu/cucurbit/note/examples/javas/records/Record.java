package icu.cucurbit.note.examples.javas.records;

public record Record(String a) {

    public Record() {
        this("default a");
    }
}
