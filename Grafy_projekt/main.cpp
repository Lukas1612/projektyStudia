#include <iostream>
#include <stack>
#include <queue>

using namespace std;

union type
{
    double _double;     // occupies 4 bytes
    int _int; // occupies 4 bytes
    bool _bool;     // occupies 1 byte
};

class Graf_DFS
{
protected:
    int n, m;
    bool A[100][100];
    bool odwiedzone[100];

    virtual void sprawdz_wierzcholki(int wierzch, int i) {};
    virtual void sprawdz_nieodwiedzone_wierzcholki(int wierzch, int i) {};

public:
    Graf_DFS();
    ~Graf_DFS() {};

    int get_value_m()
    {
        return m;
    }
    int get_value_n()
    {
        return n;
    }

    bool get_value_A(int i, int j)
    {
        return A[i][j];
    }


    void Wczytaj();
    void ZerujTablice();
    void PrzejdzGraf(int poczatek=0);
    virtual type Sprawdz()
    {
        PrzejdzGraf();
    };


};

Graf_DFS::Graf_DFS()
{
    n=0;
    m=0;
    for(int i=0; i<100; ++i)
    {
        odwiedzone[i]=0;

        for(int j=0; j<100; ++j)
        {
            A[i][j]=0;
        }
    }
}


void Graf_DFS::Wczytaj()
{
    cin>> n>>m;

    for(int i=0; i<m; ++i)
    {
        int a, b;
        cin>>a>>b;

        A[a][b]=1;
        A[b][a]=1;
    }
}

void Graf_DFS::ZerujTablice()
{
    n=0;
    m=0;
    for(int i=0; i<100; ++i)
    {
        odwiedzone[i]=0;

        for(int j=0; j<100; ++j)
        {
            A[i][j]=0;
        }
    }
}

void Graf_DFS::PrzejdzGraf(int poczatek)
{
    int tmp, i;
    int wierzch;
    stack<int> S;
    S.push(poczatek);
    i=0;

    while(!S.empty())
    {

        wierzch=S.top();
        S.pop();


        if(odwiedzone[wierzch]==false)
        {
            odwiedzone[wierzch]=true;


            for( i=0; i<n; ++i)
            {
                if( A[wierzch][i]==true)
                {
                    sprawdz_wierzcholki(wierzch, i);

                    if(odwiedzone[i] == false)
                    {
                        S.push(i);
                        sprawdz_nieodwiedzone_wierzcholki(wierzch, i);
                    }

                }
            }
        }
    }
}


class CzyDwudzielny : public Graf_DFS
{
protected:
    int kolory[100];
    type czyDwudzielny;

    void sprawdz_wierzcholki(int wierzch, int i);
    void sprawdz_nieodwiedzone_wierzcholki(int wierzch, int i) {};

public:
    CzyDwudzielny()
        : Graf_DFS()
    {
        for(int i=0; i<100; ++i)
        {
            odwiedzone[i]=0;
            kolory[i]=0;
        }
    }

    CzyDwudzielny(Graf_DFS *G)
        : Graf_DFS()
    {
        n=G->get_value_n();
        m=G->get_value_m();

        for(int i=0; i<100; ++i)
        {
            odwiedzone[i]=0;
            kolory[i]=0;

            for(int j=0; j<100; ++j)
            {
                A[i][j]=G->get_value_A(i, j);
            }
        }
    }


    type Sprawdz();
    type oblicz_czy_dwodzielny();
    void ZerujTablice();
};


void CzyDwudzielny::ZerujTablice()
{
    Graf_DFS *D;
    D=this;
    D->ZerujTablice();

    for(int i=0; i<100; ++i)
    {
        kolory[i]=0;
    }

}

void CzyDwudzielny::sprawdz_wierzcholki(int wierzch, int i)
{
    if(kolory[i] == 0)
    {
        kolory[i] = -kolory[wierzch];
    }
    if(kolory[i] == kolory[wierzch])
    {
        czyDwudzielny._bool = false;
    };

};


type CzyDwudzielny::oblicz_czy_dwodzielny()
{
    czyDwudzielny._bool= true;

    for(int i=0; i<100; ++i)
    {
        kolory[i]=0;
        odwiedzone[i]=0;
    }

    kolory[0] = 1;

    PrzejdzGraf(0);

    return czyDwudzielny;
};


type CzyDwudzielny::Sprawdz()
{
    return oblicz_czy_dwodzielny();
};



class CzySpojny : public Graf_DFS
{
protected:
    type spojny;
    void sprawdz_wierzcholki(int wierzch, int i) {};
    void sprawdz_nieodwiedzone_wierzcholki(int wierzch, int i) {};

public:
    CzySpojny()
        : Graf_DFS() {};

    CzySpojny(Graf_DFS *G)
        : Graf_DFS()
    {
        n=G->get_value_n();
        m=G->get_value_m();

        for(int i=0; i<100; ++i)
        {
             odwiedzone[i]=0;

            for(int j=0; j<100; ++j)
            {
                A[i][j]=G->get_value_A(i, j);
            }
        }

    };

    ~CzySpojny() {};

    type Sprawdz();
    type oblicz_czy_spojny();
};

type CzySpojny::oblicz_czy_spojny()
{

    for(int i=0; i<100; ++i)
    {
        odwiedzone[i]=0;
    }

    PrzejdzGraf();

    spojny._bool=true;

    for(int i=0; i<n; ++i)
    {
        if(odwiedzone[i]==false)
        {
            spojny._bool=false;
            break;
        }
    }


    return spojny;
}

type CzySpojny::Sprawdz()
{
    return oblicz_czy_spojny();
}


class LiczbaSpojnychPodgrafow : public Graf_DFS
{
protected:
    type wynik;
    void sprawdz_wierzcholki(int wierzch, int i) {};
    void sprawdz_nieodwiedzone_wierzcholki(int wierzch, int i) {};

public:
    LiczbaSpojnychPodgrafow()
        : Graf_DFS() {}

    LiczbaSpojnychPodgrafow(Graf_DFS *G)
        : Graf_DFS()
    {
        n=G->get_value_n();
        m=G->get_value_m();

        for(int i=0; i<100; ++i)
        {
            odwiedzone[i]=0;

            for(int j=0; j<100; ++j)
            {
                A[i][j]=G->get_value_A(i, j);
            }
        }

    };

    type Sprawdz();
    type oblicz_lsp();
    int PierwszyNieodwiedzonyWierzcholek(int i=0);

};

int LiczbaSpojnychPodgrafow::PierwszyNieodwiedzonyWierzcholek(int i)
{
    int j;
    PrzejdzGraf(i);

    bool spojny=true;
    for(j=i; j<n; ++j)
    {
        if(odwiedzone[j]==false)
        {
            spojny=false;
            return j;
        }
    }

    return j;

}

type LiczbaSpojnychPodgrafow::oblicz_lsp()
{
    wynik._int=0;
    int wierzcholek=0;

    while(wierzcholek < (n-1))
    {
        wierzcholek=PierwszyNieodwiedzonyWierzcholek(wierzcholek);
        ++wynik._int;
    }

    return wynik;

}


type LiczbaSpojnychPodgrafow::Sprawdz()
{
    return oblicz_lsp();
}

class CzyDrzewo : public CzySpojny
{
public:
    CzyDrzewo()
        : CzySpojny() {};

    CzyDrzewo(Graf_DFS *G)
        : CzySpojny()
    {
        n=G->get_value_n();
        m=G->get_value_m();

        for(int i=0; i<100; ++i)
        {
            odwiedzone[i]=0;
            for(int j=0; j<100; ++j)
            {
                A[i][j]=G->get_value_A(i, j);
            }
        }

    };

    ~CzyDrzewo() {};

    type Sprawdz();
    type oblicz_czy_drzewo();

};

type CzyDrzewo::oblicz_czy_drzewo()
{
    type wynik;

    if((n-m==1) && oblicz_czy_spojny()._bool)
    {
        wynik._bool=true;
    }
    else
    {
        wynik._bool=false;
    }


    return  wynik;
}


type CzyDrzewo::Sprawdz()
{
    return oblicz_czy_drzewo();
}



int main()
{
    Graf_DFS *G;
    G=new Graf_DFS;
    G->Wczytaj();


    G=new CzyDwudzielny(G);
    cout<<" Czy dwudzielny: "<<G->Sprawdz()._bool<<endl;

    G=new CzySpojny(G);
    cout<<" Czy spojny: "<<G->Sprawdz()._bool<<endl;

    G=new CzyDrzewo(G);
    cout<<" Czy drzewo: "<<G->Sprawdz()._bool<<endl;

    G=new LiczbaSpojnychPodgrafow(G);
    cout<<" Liczba spojnych podgrafow: "<<G->Sprawdz()._int<<endl;


    return 0;
}
