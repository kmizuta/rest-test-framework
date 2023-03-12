print('Hello, ' + name + '! We are all good!');
foo.bar('Nami');

function double(value) {
   return value * 2;
}
var map = { a: 'A', b: 'B', c: 'C', d: [ 1,2,3], e: double, f: double(3)};
foo.handleMap(map);
