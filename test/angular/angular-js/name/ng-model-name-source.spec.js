'use strict';

const expect = require('chai').expect;
const extractName = require('../../../name/name-source-spec-helper');
const NgModelNameSource = require('../../../../lib/angular/angular-js/name/ng-model-name-source');

describe('NgModelNameSource', () => {
  let source = new NgModelNameSource();

  it('should extract name', () => {
    expect(extractName('<p ng-model="text"></p>', source)).to.equal('text');
    expect(extractName('<p data-ng-model="text"></p>', source)).to.equal('text');
    expect(extractName('<p x-ng-model="text"></p>', source)).to.equal('text');
  });
});