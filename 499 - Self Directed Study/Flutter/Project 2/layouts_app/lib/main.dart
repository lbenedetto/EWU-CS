import 'package:flutter/material.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final titleSection = new ImageTitle(
        title: "Oeschinen Lake Campground",
        subtitle: "Kandersteg, Switzerland",
        numFavorites: 42);

    final buttonBar = new ButtonBar(icons: <IconData>[
      Icons.call,
      Icons.near_me,
      Icons.share,
    ], labels: <String>[
      "CALL",
      "ROUTE",
      "SHARE",
    ]);

    final textSection = new Container(
      padding: const EdgeInsets.all(32.0),
      child: new Text(
        'Lake Oeschinen lies at the foot of the Blüemlisalp in the Bernese Alps. Situated 1,578 meters above sea level, it is one of the larger Alpine Lakes. A gondola ride from Kandersteg, followed by a half-hour walk through pastures and pine forest, leads you to the lake, which warms to 20 degrees Celsius in the summer. Activities enjoyed here include rowing, and riding the summer toboggan run.',
        softWrap: true,
      ),
    );

    return new MaterialApp(
      title: "Layout Testing",
      theme: new ThemeData(
        primaryColor: Colors.red,
        brightness: Brightness.dark,
      ),
      home: new Scaffold(
          appBar: new AppBar(title: new Text('Example Lake')),
          body: new ListView(
            children: <Widget>[
              new Image.asset(
                'images/lake.jpg',
                width: 600.0,
                height: 240.0,
                fit: BoxFit.cover,
              ),
              titleSection,
              buttonBar,
              textSection,
            ],
          )),
    );
  }
}

class ImageTitle extends StatelessWidget {
  ImageTitle({Key key, this.title, this.subtitle, this.numFavorites})
      : super(key: key);
  final String title;
  final String subtitle;
  final int numFavorites;

  @override
  Widget build(BuildContext context) {
    return new Container(
      padding: const EdgeInsets.all(32.0),
      child: new Row(
        children: [
          new Expanded(
            child: new Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                new Container(
                  padding: const EdgeInsets.only(bottom: 8.0),
                  child: new Text(
                    title,
                    style: new TextStyle(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                new Text(
                  subtitle,
                  style: new TextStyle(
                    color: Colors.grey[500],
                  ),
                ),
              ],
            ),
          ),
          new Icon(
            Icons.star,
            color: Colors.red[500],
          ),
          new Text('$numFavorites'),
        ],
      ),
    );

  }
}

class LabeledIcon extends StatelessWidget {
  LabeledIcon({Key key, this.icon, this.label}) : super(key: key);
  final IconData icon;
  final String label;

  @override
  Widget build(BuildContext context) {
    Color color = Theme.of(context).primaryColor;

    return new Column(
      mainAxisSize: MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        new Icon(icon, color: color),
        new Container(
          margin: const EdgeInsets.only(top: 8.0),
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

class ButtonBar extends StatelessWidget {
  ButtonBar({Key key, this.icons, this.labels}) : super(key: key);
  final List<IconData> icons;
  final List<String> labels;

  @override
  Widget build(BuildContext context) {
    List<Widget> widgets = new List(icons.length);
    for (int i = 0; i < icons.length; i++) {
      widgets[i] = new LabeledIcon(icon: icons[i], label: labels[i]);
    }

    return new Container(
      child: new Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: widgets,
      ),
    );
  }
}
