from DatebaseManager import DatebaseManager
from datetime import datetime
from dateutil.parser import parse


class AccountManager:
    def __init__(self, db_file):
        self.datebaseManager = DatebaseManager(db_file)
        self.maxDayInMonth = (0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)


    def getCurrentSaldo(self):
        rows = self.datebaseManager.select_latest_amount_from_operations()
        if len(rows) == 0:
            return 0
        else:
            row = rows[0]
            return row[0]



    def addIncome(self, amount, id_category, date, note):


        date = int(date)
        dateText = datetime.fromtimestamp(date)



        operation =('wplata', amount, id_category, date, dateText, note)
        self.datebaseManager.insert_to_operations(operation)


    def addExpenditure(self, amount, id_category, date, note):


        date = int(date)
        dateText = datetime.fromtimestamp(date)


        amount = -amount
        operation = ('wydatek', amount, id_category, date, dateText, note)
        self.datebaseManager.insert_to_operations(operation)

    def addNewCategory(self, name):

        category = (name,)
        self.datebaseManager.insert_to_categories(category)

    def showbBalance(self):
        rows = self.datebaseManager.select_amount_from_operations()

        balance = float(0)
        if len(rows) != 0:
            for row in rows:
                balance = balance + float(row[0])
        return balance

    def showStatistics(self):
        print("statistics")
    def getAllCategories(self):
        rows = self.datebaseManager.select_all_from_categories()
        return rows
    def showEntireHistoryOperations(self):

        rows = self.datebaseManager.select_all_from_operations()

        for row in rows:
            print(row)
    def getOperationsInSetInterval(self, year, month, day, year2, month2, day2):

        dateMin = parse(str(year) + '-' + str(month) + '-' + str(day))
        dateMin = dateMin.timestamp()
        dateMax = parse(str(year2) + '-' + str(month2) + '-' + str(day2) + ' 23:59:59')
        dateMax = dateMax.timestamp()


        rows = self.datebaseManager.select_all_from_operations_in_set_date(dateMin, dateMax)

        return rows

    def getOperationsFromMonth(self, year, month):
        dateMin = parse(str(year) + '-' + str(month) + '-' + '01')
        dateMin = dateMin.timestamp()
        dateMax = parse(str(year) + '-' + str(month) + '-' + str(self.maxDayInMonth[int(month)]) + ' 23:59:59')
        dateMax = dateMax.timestamp()

        rows = self.datebaseManager.select_all_from_operations_in_set_date(dateMin, dateMax)
        return rows

    def getOperationsFromDay(self, year, month, day):
        dateMin = parse(str(year) + '-' + str(month) + '-' + str(day))
        dateMin = dateMin.timestamp()
        dateMax = parse(str(year) + '-' + str(month) + '-' + str(day) + ' 23:59:59')
        dateMax = dateMax.timestamp()

        rows = self.datebaseManager.select_all_from_operations_in_set_date(dateMin, dateMax)
        return rows

    def getOperationsFromYear(self, year):
        dateMin = parse(str(year) + '-' + '01' + '-' + '01')
        dateMin = dateMin.timestamp()
        dateMax = parse(str(year) + '-' + '12' + '-' + '31' + ' 23:59:59')
        dateMax = dateMax.timestamp()

        rows = self.datebaseManager.select_all_from_operations_in_set_date(dateMin, dateMax)

        return rows

    def getOperationsWhereCategory(self, id):
        rows = self.datebaseManager.select_all_from_operations_where_category_id_is(str(id))
        return rows

    def doesCategoryNameAlreadyExist(self, name):

        rows = self.datebaseManager.select_from_categories_where_name_is(name)

        result = False

        if len(rows) > 0:
                result = True
        return result

    def doesCategoryIdExist(self, id):

        rows = self.datebaseManager.select_from_categories_where_id_is(id)

        result = False

        if len(rows) > 0:
            result = True
        return result
    def getCategoryIdByName(self, name):

        rows = self.datebaseManager.select_from_categories_where_name_is(name)


        return rows[0][0]

    def getCategoryNameById(self, id):

        rows = self.datebaseManager.select_from_categories_where_id_is(id)
        return rows[0][1]






