from AccountManager import AccountManager
from UserInterInterface import UserInterface




class App:
    def __init__(self, db_file):

        self.accountManager = AccountManager(db_file)
        self.userInterface = UserInterface(self.accountManager)




app = App(r"C:\lokalizacja\do\pliku\budgetCalculator\db.db")