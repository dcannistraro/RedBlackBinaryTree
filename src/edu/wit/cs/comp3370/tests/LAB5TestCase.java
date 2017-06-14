package edu.wit.cs.comp3370.tests;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import edu.wit.cs.comp3370.DiskLocation;
import edu.wit.cs.comp3370.LocationHolder;
import edu.wit.cs.comp3370.RBTree;

public class LAB5TestCase{


	@Rule
	public Timeout globalTimeout = Timeout.seconds(15);

	@SuppressWarnings("serial")
	private static class ExitException extends SecurityException {}

	private static class NoExitSecurityManager extends SecurityManager 
	{
		@Override
		public void checkPermission(Permission perm) {}

		@Override
		public void checkPermission(Permission perm, Object context) {}

		@Override
		public void checkExit(int status) { super.checkExit(status); throw new ExitException(); }
	}

	@Before
	public void setUp() throws Exception 
	{
		System.setSecurityManager(new NoExitSecurityManager());
	}

	@After
	public void tearDown() throws Exception 
	{
		System.setSecurityManager(null);
	}

	private void _testInsert(LocationHolder T, DiskLocation[] vals) {
		try {
			for (int i = 0; i < vals.length; i++) {
				T.insert(vals[i]);
			}
		} catch (ExitException e) {}
	}

	private void _testNext(LocationHolder T, DiskLocation target, DiskLocation expected) {
		DiskLocation actual = new DiskLocation(-1, -1);
		try {
			actual = T.next(target);
		} catch (ExitException e) {}
		assertEquals(expected.track, actual.track);
		assertEquals(expected.sector, actual.sector);
	}

	private void _testPrev(LocationHolder T, DiskLocation target, DiskLocation expected) {
		DiskLocation actual = new DiskLocation(-1, -1);
		try {
			actual = T.prev(target);
		} catch (ExitException e) {}
		assertEquals(expected.track, actual.track);
		assertEquals(expected.sector, actual.sector);
	}

	private void _testHeight(LocationHolder T, int max) {
		int actual = Integer.MAX_VALUE;
		try {
			actual = T.height();
		} catch (ExitException e) {}
		assertTrue(max >= actual);
	}

	@Test
	public void testInsertSmall() {
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 2)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(1, 3)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(1, 1), new DiskLocation(1, 3)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 2), new DiskLocation(1, 1)});
	}

	@Test
	public void testInsertMedium() {
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 1), new DiskLocation(1, 2), new DiskLocation(1, 4)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 1), new DiskLocation(1, 2), new DiskLocation(1, 5), new DiskLocation(1, 4)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(2, 1), new DiskLocation(2, 3), new DiskLocation(1, 4), new DiskLocation(1, 1), new DiskLocation(2, 4)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(2, 3), new DiskLocation(1, 3), new DiskLocation(2, 2), new DiskLocation(1, 2), new DiskLocation(1, 1), new DiskLocation(2, 1)});
	}

	@Test
	public void testInsertSeq() {
		for (int i = 1; i < 1000000; i*=10) {
			DiskLocation[] d = new DiskLocation[i];
			for (int j = 0; j < i; j++)
				d[j] = new DiskLocation(1, j);
			_testInsert(new RBTree(), d);
		}
	}

	@Test
	public void testInsertRand() {

		for (int track = 10; track <= 200; track += 10) {
			for (int sector = 10; sector <= 200; sector += 10) {
				ArrayList<DiskLocation> arrD = new ArrayList<DiskLocation>(track*sector);
				for (int i = 0; i < track; i++) {
					for (int j = 0; j < sector; j++) 
						arrD.add(new DiskLocation(i, j));
				}
				DiskLocation[] d = new DiskLocation[track*sector];
				Collections.shuffle(arrD);		// randomize array
				_testInsert(new RBTree(), arrD.toArray(d));
			}
		}
	}
	
	private RBTree _buildSmallTree() {
		RBTree T = new RBTree();
		
		T.insert(new DiskLocation(6, 1));
		T.insert(new DiskLocation(4, 1));
		T.insert(new DiskLocation(9, 1));
		T.insert(new DiskLocation(2, 1));
		T.insert(new DiskLocation(5, 1));
		T.insert(new DiskLocation(8, 1));
		T.insert(new DiskLocation(11, 1));
		
		return T;
	}
	
	private RBTree _buildSmolTree() {
		RBTree T = new RBTree();
		
		T.insert(new DiskLocation(6, 1));
		T.insert(new DiskLocation(4, 1));
		T.insert(new DiskLocation(9, 1));
		
		return T;
	}

	@Test
	public void testHeightSmall() {
		RBTree T = _buildSmolTree();
		_testHeight(T, 2);
		
		T = _buildSmallTree();
		_testHeight(T, 4);
	}
	
	@Test
	public void testHeightLarge() {
		RBTree T = new RBTree();
		
		for (int i = 0; i < 100; i++)
			T.insert(new DiskLocation(i, 1));
		_testHeight(T, 14);
		
		for (int i = 100; i < 1000; i++)
			T.insert(new DiskLocation(i, 1));
		_testHeight(T, 20);
	}

	@Test
	public void testNextSmall() {
		RBTree T = _buildSmolTree();

		_testNext(T, T.find(new DiskLocation(4, 1)), new DiskLocation(6, 1));
		_testNext(T, T.find(new DiskLocation(6, 1)), new DiskLocation(9, 1));

		T = _buildSmallTree();
		_testNext(T, T.find(new DiskLocation(9, 1)), new DiskLocation(11, 1));
		_testNext(T, T.find(new DiskLocation(4, 1)), new DiskLocation(5, 1));
	}

	@Test
	public void testNextLarge() {
		RBTree T = new RBTree();

		for (int i = 0; i < 300; i++)
			T.insert(new DiskLocation(i, 1));
		_testNext(T, T.find(new DiskLocation(91, 1)), new DiskLocation(92, 1));
		_testNext(T, T.find(new DiskLocation(140, 1)), new DiskLocation(141, 1));
	}

	@Test
	public void testPrevSmall() {

		RBTree T = _buildSmolTree();
		_testPrev(T, T.find(new DiskLocation(9, 1)), new DiskLocation(6, 1));
		_testPrev(T, T.find(new DiskLocation(6, 1)), new DiskLocation(4, 1));

		T = _buildSmallTree();
		_testPrev(T, T.find(new DiskLocation(11, 1)), new DiskLocation(9, 1));
		_testPrev(T, T.find(new DiskLocation(5, 1)), new DiskLocation(4, 1));
	}

	@Test
	public void testPrevLarge() {
		RBTree T = new RBTree();

		for (int i = 0; i < 300; i++)
			T.insert(new DiskLocation(i, 1));
		_testPrev(T, T.find(new DiskLocation(92, 1)), new DiskLocation(91, 1));
		_testPrev(T, T.find(new DiskLocation(141, 1)), new DiskLocation(140, 1));
	}
	
	@Test
	public void testFind() {
		RBTree T = _buildSmolTree();
		assertTrue("find is not working correctly", T.find(new DiskLocation(9, 1)).equals(new DiskLocation(9, 1)));
		assertTrue("find is not working correctly", T.find(new DiskLocation(4, 1)).equals(new DiskLocation(4, 1)));

		T = _buildSmallTree();
		assertTrue("find is not working correctly", T.find(new DiskLocation(4, 1)).equals(new DiskLocation(4, 1)));
		assertTrue("find is not working correctly", T.find(new DiskLocation(11, 1)).equals(new DiskLocation(11, 1)));

	}
}
