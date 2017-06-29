var gulp = require('gulp'); 
var uglify = require('gulp-uglify');
var concat = require('gulp-concat'); 

gulp.task('min', function(){
  return gulp.src(['runtime.js', 'classie.js', 'nav.js'])
  .pipe(uglify())
  .pipe(concat('scripts.js'))
  .pipe(gulp.dest('./'))
});

gulp.task('debug', function(){
  return gulp.src(['runtime.js', 'classie.js', 'nav.js'])
  .pipe(concat('scripts.js'))
  .pipe(gulp.dest('./'))
});
gulp.task('default', ['min']);
