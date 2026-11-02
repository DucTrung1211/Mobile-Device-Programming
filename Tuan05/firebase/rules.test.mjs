import { readFile } from 'node:fs/promises';
import { after, before, beforeEach, test } from 'node:test';
import { initializeTestEnvironment, assertSucceeds, assertFails } from '@firebase/rules-unit-testing';
import { ref, set, get, update, remove } from 'firebase/database';
import { ref as storageRef, uploadBytes, getBytes, deleteObject } from 'firebase/storage';

let env;
before(async () => {
  env = await initializeTestEnvironment({
    projectId: 'demo-tuan05',
    database: { host: '127.0.0.1', port: 9000, rules: await readFile('database.rules.json', 'utf8') },
    storage: { host: '127.0.0.1', port: 9199, rules: await readFile('storage.rules', 'utf8') }
  });
});
beforeEach(async () => { await env.clearDatabase(); await env.clearStorage(); });
after(async () => { await env?.cleanup(); });
const task = { title: 'Learn Firebase', note: '', category: 'STUDY', date: '2026-11-02', time: '09:00', reminder: false, priority: 'HIGH' };
test('owner can CRUD and complete a task', async () => {
  const db = env.authenticatedContext('alice').database();
  const target = ref(db, 'users/alice/tasks/a');
  await assertSucceeds(set(target, task));
  await assertSucceeds(get(target));
  await assertSucceeds(update(target, { completedDate: '2026-11-02' }));
  await assertSucceeds(update(target, { title: 'Edited' }));
  await assertSucceeds(remove(target));
});
test('other user and unauthenticated clients cannot read or write tasks', async () => {
  const owner = env.authenticatedContext('alice').database();
  await set(ref(owner, 'users/alice/tasks/a'), task);
  for (const ctx of [env.authenticatedContext('bob'), env.unauthenticatedContext()]) {
    const target = ref(ctx.database(), 'users/alice/tasks/a');
    await assertFails(get(target));
    await assertFails(set(target, task));
    await assertFails(remove(target));
  }
  await assertFails(get(ref(owner, 'users')));
});
test('rules reject malformed and extra fields', async () => {
  const target = ref(env.authenticatedContext('alice').database(), 'users/alice/tasks/a');
  for (const invalid of [
    { ...task, title: '' }, { ...task, title: 'a'.repeat(201) }, { ...task, category: 'ADMIN' },
    { ...task, time: '99:99' }, { ...task, date: 'bad' }, { ...task, unexpected: true }, { ...task, reminder: 'true' }
  ]) await assertFails(set(target, invalid));
});
test('device tokens and avatar version are UID-scoped', async () => {
  const alice = env.authenticatedContext('alice').database();
  const bob = env.authenticatedContext('bob').database();
  await assertSucceeds(set(ref(alice, 'users/alice/deviceRegistrations/device-a'), { identifier: 'test-fid-not-a-real-credential', kind: 'fid', updatedAt: 1 }));
  await assertFails(get(ref(bob, 'users/alice/deviceRegistrations')));
  await assertFails(set(ref(bob, 'users/alice/profile/avatarVersion'), 1));
  await assertFails(set(ref(alice, 'users/alice/deviceRegistrations/device-a'), 123));
});
test('only owner can upload, read and delete avatar; mime and size enforced', async () => {
  const alice = storageRef(env.authenticatedContext('alice').storage(), 'users/alice/avatar');
  const bob = storageRef(env.authenticatedContext('bob').storage(), 'users/alice/avatar');
  const anon = storageRef(env.unauthenticatedContext().storage(), 'users/alice/avatar');
  await assertSucceeds(uploadBytes(alice, new Uint8Array([1,2,3]), { contentType: 'image/png' }));
  await assertSucceeds(getBytes(alice));
  await assertFails(getBytes(bob));
  await assertFails(getBytes(anon));
  await assertFails(uploadBytes(bob, new Uint8Array([1]), { contentType: 'image/png' }));
  await assertFails(uploadBytes(alice, new Uint8Array([1]), { contentType: 'text/plain' }));
  await assertFails(uploadBytes(alice, new Uint8Array(5 * 1024 * 1024 + 1), { contentType: 'image/png' }));
  await assertSucceeds(deleteObject(alice));
});
