package hotel;

public class Food implements Billable {

    final private FoodMenuItem menuItem;

    public enum FoodMenuItem {

        SANDWICH(150) {
            @Override
            public String toString() {
                return "Sandwich";
            }
        },
        PASTA(160) {
            @Override
            public String toString() {
                return "Pasta";
            }
        },
        NOODLES(170) {
            @Override
            public String toString() {
                return "Noodles";
            }
        },
        DRINK(30) {
            @Override
            public String toString() {
                return "Drink";
            }
        };

        private final int price;

        FoodMenuItem(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

    }

    Food(FoodMenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public String toString() {
        return menuItem.toString();
    }

    public FoodMenuItem getMenuItem() {
        return menuItem;
    }

    public int getPrice() {
        return menuItem.getPrice();
    }

    public String getServiceType() {
        return "Food";
    }

}
