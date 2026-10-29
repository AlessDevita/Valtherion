package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.quest.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestComponent implements Component {
    private Entity entity;
    private final List<Quest> quests = new ArrayList<>();

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void init() {}

    @Override
    public void cleanup() { quests.clear(); }

    public boolean hasQuest(String questId) {
        if (questId == null) return false;
        return quests.stream().anyMatch(q -> questId.equals(q.getId()));
    }

    public void removeQuest(String questId) {
        if (questId == null) return;
        quests.removeIf(q -> questId.equals(q.getId()));
    }

    public void addQuest(Quest quest) {
        if (quest != null) quests.add(quest);
    }

    public Quest[] getQuests() {
        return quests.toArray(new Quest[0]);
    }
}
