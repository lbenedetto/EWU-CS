import 'package:flutter/material.dart';

void main() {
  runApp(new MyApp());
}

_SubCategoryListState _subCategoryListState = new _SubCategoryListState();
List<String> categories = generateStrings(1);

List<String> generateStrings(int n) {
  List<String> l = new List<String>();
  for (int i = 0; i < 100; i++) {
    l.add("Category $n Item $i");
  }
  return l;
}

List<Widget> generateCategories(int n) {
  List<Widget> widgets = new List<Widget>();
  for (int i = 1; i < n; i++) {
    widgets.add(new LabeledIconButton(
      icon: Icons.category,
      label: "Category $i",
      callback: () {
        _subCategoryListState.update(() {
          categories = generateStrings(i);
        });
      },
    ));
  }
  return widgets;
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    SubCategoryList subs = new SubCategoryList();
    return new MaterialApp(
      title: "Demo App",
      theme: new ThemeData(
        primaryColor: Colors.red,
        brightness: Brightness.dark,
      ),
      home: new Scaffold(
        appBar: new AppBar(title: new Text('Demo App')),
        body: new Column(
          children: [
            new Container(
              height: 100.0,
              child: new ListView(
                scrollDirection: Axis.horizontal,
                children: generateCategories(20),
              ),
            ),
            new Center(
              child: new Icon(Icons.linear_scale),
            ),
            new Divider(),
            new Expanded(child: new Container(child: subs))
          ],
        ),
      ),
    );
  }
}

class SubCategoryList extends StatefulWidget {
  @override
  _SubCategoryListState createState() => _subCategoryListState;
}

class _SubCategoryListState extends State<SubCategoryList> {
  void update(VoidCallback fn) {
    setState(fn);
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: _buildList(),
    );
  }

  Widget _buildList() {
    List<Widget> children = new List<Widget>();
    for (String s in categories) {
      children.add(new LabeledIcon(icon: Icons.category, label: s));
    }

    return new GridView.count(
        primary: false,
        padding: const EdgeInsets.only(left: 8.0, right: 8.0, top: 8.0),
        crossAxisSpacing: 10.0,
        crossAxisCount: 10,
        children: children);
  }
}

class LabeledIconButton extends StatelessWidget {
  LabeledIconButton({Key key, this.icon, this.label, this.callback}) : super(key: key);
  final IconData icon;
  final String label;
  final VoidCallback callback;

  @override
  Widget build(BuildContext context) {
    Color color = Theme.of(context).primaryColor;

    return new Column(
      mainAxisSize: MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        new IconButton(
          iconSize: 48.0,
          icon: new Icon(icon),
          onPressed: () {
            callback();
          },
        ),
        new Container(
          margin: const EdgeInsets.only(left: 8.0, right: 8.0, top: 8.0),
          child: new Text(
            label,
            style: new TextStyle(
              fontSize: 12.0,
              fontWeight: FontWeight.w400,
              color: color,
            ),
          ),
        ),
      ],
    );
  }
}

class LabeledIcon extends StatelessWidget {
  LabeledIcon({Key key, this.icon, this.label}) : super(key: key);
  final IconData icon;
  final String label;

  @override
  Widget build(BuildContext context) {
    return new Column(
      mainAxisSize: MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        new Icon(icon, color: Colors.white),
        new Container(
          margin: const EdgeInsets.only(top: 8.0),
          child: new Text(
            label,
            style: new TextStyle(
              fontSize: 12.0,
              fontWeight: FontWeight.w400,
              color: Colors.white,
            ),
          ),
        ),
      ],
    );
  }
}
