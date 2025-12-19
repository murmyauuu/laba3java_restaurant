package restaurant.model;

public enum DishType {
    PIROZHKI(1800),     // Пирожки с капустой
    KOMPOT(1200),       // Компот из сухофруктов
    KARTOFEL_FRI(2200), // Картофель фри
    CHEESECAKE(3000),   // Чизкейк
    COFFEE(1000);       // Кофе (быстрый заказ)

    private final long cookTimeMs;

    DishType(long cookTimeMs) {
        this.cookTimeMs = cookTimeMs;
    }

    public long getCookTimeMs() {
        return cookTimeMs;
    }

    public String getDisplayName() {
        switch (this) {
            case PIROZHKI:        return "ПИРОЖКИ";
            case KOMPOT:          return "КОМПОТ";
            case KARTOFEL_FRI:  return "КАРТОФЕЛЬ ФРИ";
            case CHEESECAKE:      return "ЧИЗКЕЙК";
            case COFFEE:          return "КОФЕ";
            default:              return name();
        }
    }
}