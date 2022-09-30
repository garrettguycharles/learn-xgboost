from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from xgboost import XGBClassifier

data = load_iris()

X_train, X_test, y_train, y_test = train_test_split(data['data'], data['target'], test_size=0.2)

model = XGBClassifier(n_estimators=2, max_depth=2, learning_rate=1, objective='binary:logistic')

if __name__ == '__main__':
    model.fit(X_train, y_train)

    predictions = model.predict(X_test)

    print(predictions, y_train, accuracy_score(predictions, y_test), sep="\n")

